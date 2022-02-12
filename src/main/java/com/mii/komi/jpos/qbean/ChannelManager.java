package com.mii.komi.jpos.qbean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ISOServerClientDisconnectEvent;
import org.jpos.iso.ISOServerEventListener;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.QBeanSupport;
import org.jpos.q2.iso.ChannelAdaptor;
import org.jpos.q2.iso.QMUX;
import org.jpos.q2.iso.QServer;
import org.jpos.space.LocalSpace;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.SpaceListener;
import org.jpos.util.Log;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class ChannelManager extends QBeanSupport implements SpaceListener, ISOServerEventListener {

    private Log log = Log.getLog("Q2", "channel-manager");

    private static ChannelManager _cMSingleTon = null;
    private long MAX_TIME_OUT;
    private QMUX mux;
    private LocalSpace sp;
    private String in;
    private String out;
    private ISOPackager packager;
    
    public static int stanSequence = 1;

    public static void logISOMsg(ISOMsg msg) {
        try {
            String mti = String.format("[0:%s]", msg.getMTI());
            StringBuilder isoMsg = new StringBuilder(mti);
            for (int i = 1; i <= msg.getMaxField(); i++) {
                if (msg.hasField(i)) {
                    String field = String.format("[%d:%s]", i, msg.getString(i));
                    isoMsg.append(field);
                }
            }
            
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    protected void initService() throws ISOException {
        try {
            mux = NameRegistrar.get("mux." + cfg.get("destination") + "-mux");

            in = mux.getInQueue();
            out = mux.getOutQueue();
            sp = (LocalSpace) mux.getSpace();
            sp.addListener(in, this);

            MAX_TIME_OUT = cfg.getLong("timeout");

            NameRegistrar.register(getName(), this);

        } catch (NameRegistrar.NotFoundException e) {
            log.error("Error in initializing service :" + e.getMessage());
        }
    }
    
    private static void addStanSeqence() {
        if(999999 == stanSequence) {
            stanSequence = 1;
        } else {
            stanSequence++;
        }
    }

    private void sendEchoTest() {
        Map<String, String> date = getDate();

        try {
            ISOMsg msg = new ISOMsg();

            msg.setMTI("0800");
            msg.set(7, date.get("bit7"));
            msg.set(11, ISOUtil.zeropad(stanSequence, 6));
            msg.set(70, "301");
            msg.setPackager(packager);

            byte[] messageBody = msg.pack();

            logISOMsg(msg);

            try {
                sendMsg(msg);
                addStanSeqence();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ISOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Send sign-on response.
     *
     * @param source (Artajasa) channel
     * @param m message from client
     */
    /**
     * 
     */
    private void sendSignOnRequest(ISOMsg m) {
        ISOMsg reply = null;
        Space space = SpaceFactory.getSpace();
        try {
            ISOMsg msg = (ISOMsg) m.clone();
            msg.setMTI("0800");
            msg.set(7, ISODate.getDateTime(new Date()));
            msg.set(11, ISOUtil.zeropad(stanSequence, 6));
            msg.set(70, "001");
            msg.setPackager(packager);
            byte[] messageBody = msg.pack();
            ChannelManager.logISOMsg(msg);
            try {
                space.out(mux.getName() + "-signing-on", true);
                reply = sendMsg(msg);
                addStanSeqence();
                if ((reply != null) && (reply.getValue(39).equals("00"))) {
                    space.out(mux.getName() + "-signed-on", true);
                }
                return;
            } catch (Exception e) {
                log.error(e);
            }
        } catch (ISOException e) {
            log.error(e);
        } catch (Throwable e) {
            log.error(e);
        }

        // in-case of any error, clean-up
        log.warn("Incomplete signon process, cleaning up.");
        space.inp(mux.getName() + "-signing-on");
        space.inp(mux.getName() + "-signed-on");
    }

/*
    private void sendSignOnRequest(ISOMsg m) {
        ISOMsg reply = null;
        Space space = SpaceFactory.getSpace();
        try {
            ISOMsg msg = (ISOMsg) m.clone();
            msg.setMTI("0800");
            msg.set(7, ISODate.getDateTime(new Date()));
            msg.set(11, ISOUtil.zeropad(stanSequence, 6));
            msg.set(70, "001");
            msg.setPackager(packager);
            byte[] messageBody = msg.pack();
            ChannelManager.logISOMsg(msg);
            try {
                reply = sendMsg(msg);
                addStanSeqence();
                if (reply != null) {
                    if (reply.getValue(39).equals("00")) {
                        space.out(mux.getName() + "-signed-on", true);
                    } else {
                        space.in(mux.getName() + "-signed-on");
                    }
                } else {
                    space.in(mux.getName() + "-signed-on");
                }
            } catch (Exception e) {
                e.printStackTrace();
                space.in(mux.getName() + "-signed-on");
            }
        } catch (ISOException e) {
            log.error(e.getMessage());
            space.in(mux.getName() + "-signed-on");
        }
    }
*/
    /**
     *  
     */
    protected void startService() {

        try {
            ISOServer server = (ISOServer) NameRegistrar.get("server." + cfg.get("destination"));
            server.addServerEventListener(this);
            log.info("Registered server event listener.");
        } catch (NotFoundException e) {
            log.error("Failed registering server event listener : invalid destination name.");
        }

        final ISOMsg m = new ISOMsg();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Space space = SpaceFactory.getSpace();
                boolean connected = false;
                String destinationName = cfg.get("destination");
                try {
                    Object o = NameRegistrar.get(destinationName);
                    if(o instanceof QServer) {
                        ISOServer isoServer = NameRegistrar.get("server." + destinationName);
                        if(isoServer.getActiveConnections() > 0) {
                            connected = true;
                            packager = isoServer.getLastConnectedISOChannel().getPackager();
                        } else {
                            log.info("No Host connected to port " + isoServer.getPort());
                        }
                    } else if(o instanceof ChannelAdaptor) {
                        BaseChannel ca = NameRegistrar.get("channel." + destinationName);
                        if(mux.isConnected()) {
                            connected = true;
                            packager = ca.getPackager();
                        } else {
                            log.info("Not connected to '" + ca.getHost() + "'");
                        }
                    }
                } catch (NotFoundException ex) {
                    log.error("Error Invalid Destination Name '"+destinationName+"', "
                            + "must be same as ChannelAdaptor name");
                }

                if (connected) {
                    Object signingon = space.rdp(mux.getName() + "-signing-on");
                    if (signingon == null) {
                        sendSignOnRequest(m);
                    }
                    else {
                        Object signedon = space.rdp(mux.getName() + "-signed-on");
                        if (signedon != null) {
                            sendEchoTest();
                        }
                    }
                } 
                else {
                    // cleanup for redundancy
                    space.inp(mux.getName() + "-signing-on");
                    space.inp(mux.getName() + "-signed-on");
                }

            }
        }, 0, cfg.getLong("interval"));
    }

/*
    protected void startService() {
        final ISOMsg m = new ISOMsg();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Space space = SpaceFactory.getSpace();
                boolean connected = false;
                String destinationName = cfg.get("destination");
                try {
                    Object o = NameRegistrar.get(destinationName);
                    if(o instanceof QServer) {
                        ISOServer isoServer = NameRegistrar.get("server." + destinationName);
                        if(isoServer.getActiveConnections() > 0) {
                            connected = true;
                            packager = isoServer.getLastConnectedISOChannel().getPackager();
                        } else {
                            log.info("No Host connected to port " + isoServer.getPort());
                        }
                    } else if(o instanceof ChannelAdaptor) {
                        BaseChannel ca = NameRegistrar.get("channel." + destinationName);
                        if(mux.isConnected()) {
                            connected = true;
                            packager = ca.getPackager();
                        } else {
                            log.info("Not connected to '" + ca.getHost() + "'");
                        }
                    }
                } catch (NotFoundException ex) {
                    log.error("Error Invalid Destination Name '"+destinationName+"', "
                            + "must be same as ChannelAdaptor name");
                }

                if (connected) {
                    Object signedOnObject = space.rd(mux.getName() + "-signed-on", 5000);
                    if (signedOnObject != null) {
                        sendEchoTest();
                    } else {
                        sendSignOnRequest(m);
                    }
                } else {
                    space.in(mux.getName() + "-signed-on", 5000);
                }

            }
        }, 0, cfg.getLong("interval"));
    }
*/

    public ISOMsg sendMsg(ISOMsg m) throws Exception {
        return sendMsg(m, mux, MAX_TIME_OUT);
    }

    private ISOMsg sendMsg(ISOMsg m, QMUX mux, long timeout) throws Exception {

        long start = System.currentTimeMillis();

        ISOMsg resp = null;

        // if connection between Q2 and switching server is not established, LINK DOWN
        if (mux.isConnected() == false) {
            log.info("Link Down before sending.");
            // set result code to 404 which means link down
            resp = (ISOMsg) m.clone();
            resp.set(39, "91");

            return resp;
        }
        Object obj = mux.request(m, timeout);

        if (obj == null) {
            resp = (ISOMsg) m.clone();
            resp.set(39, "68");
            return resp;
        }

        if (obj instanceof ISOMsg) {
            resp = (ISOMsg) obj;
            if (resp.getValue(39).toString().equals("91")) {
                m.set(39, "91");
                return m;
            }
            return resp;
        }

        long duration = System.currentTimeMillis() - start;
        log.info("Response time (ms):" + duration);
        return resp;
    }

    private static HashMap<String, String> getDate() {
        DateFormat dateFormat1 = new SimpleDateFormat("MMdd");
        DateFormat dateFormat2Bit7 = new SimpleDateFormat("HH");
        DateFormat dateFormat2Bit12 = new SimpleDateFormat("HH");
        DateFormat dateFormat3 = new SimpleDateFormat("mmss");

        TimeZone timeZone = TimeZone.getTimeZone("GMT+00");
        TimeZone timeZone2 = TimeZone.getTimeZone("GMT+07");

        dateFormat1.setTimeZone(timeZone);
        dateFormat2Bit7.setTimeZone(timeZone); // CR#3
        dateFormat2Bit12.setTimeZone(timeZone2); // CR#3
        dateFormat3.setTimeZone(timeZone);

        Date newDate = new Date();
        String bit7 = dateFormat1.format(newDate) + dateFormat2Bit7.format(newDate) + dateFormat3.format(newDate);
        String bit12 = dateFormat2Bit12.format(newDate) + dateFormat3.format(newDate);
        String bit13 = dateFormat1.format(newDate);
        String bit15 = Integer.toString(Integer.parseInt(bit13) + 1);

        HashMap<String, String> result = new HashMap<String, String>();

        result.put("bit7", bit7);
        result.put("bit12", bit12);
        result.put("bit13", bit13);
        result.put("bit15", bit15);

        return result;
    }

    public static ChannelManager getInstance() {
        if (_cMSingleTon == null) {
            try {
                _cMSingleTon = ((ChannelManager) NameRegistrar.get("manager"));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        } else {
        }

        return _cMSingleTon;
    }

    @Override
    public void notify(Object key, Object value) {
    }

    /**
     *  ensure tokens are removed on disconnect
     */
    @Override
    public void handleISOServerEvent(EventObject ev) {
        if (ev instanceof ISOServerClientDisconnectEvent) {            
            Space space = SpaceFactory.getSpace();
            space.inp(mux.getName() + "-signing-on");
            space.inp(mux.getName() + "-signed-on");
            return;
        }
    }

}
