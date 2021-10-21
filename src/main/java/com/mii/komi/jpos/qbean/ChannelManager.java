package com.mii.komi.jpos.qbean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.q2.QBeanSupport;
import org.jpos.q2.iso.QMUX;
import org.jpos.space.LocalSpace;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.SpaceListener;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class ChannelManager extends QBeanSupport implements SpaceListener {

    private static final Logger logger = Logger.getLogger(ChannelManager.class);

    private static ChannelManager _cMSingleTon = null;
    private long MAX_TIME_OUT;
    private QMUX mux;
    private LocalSpace sp;
    private String in;
    private String out;
    
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
            logger.info(isoMsg);
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    protected void initService() throws ISOException {
        try {
            mux = NameRegistrar.get("mux." + cfg.get("mux"));

            in = mux.getInQueue();
            out = mux.getOutQueue();
            sp = (LocalSpace) mux.getSpace();
            sp.addListener(in, this);

            MAX_TIME_OUT = cfg.getLong("timeout");

            NameRegistrar.register(getName(), this);

        } catch (NameRegistrar.NotFoundException e) {
            logger.error("Error in initializing service :" + e.getMessage());
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
            msg.setPackager(new ISO87APackager());

            byte[] messageBody = msg.pack();

            logISOMsg(msg);

            try {
                sendMsg(msg);
                addStanSeqence();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ISOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Send sign-on response.
     *
     * @param source (Artajasa) channel
     * @param m message from client
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
            msg.setPackager(new ISO87APackager());
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
            logger.error(e.getMessage());
            space.in(mux.getName() + "-signed-on");
        }
    }

    protected void startService() throws ISOException {
        final ISOMsg m = new ISOMsg();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Space space = SpaceFactory.getSpace();
                if (mux.isConnected()) {
                    Object signedOnObject = space.rd(mux.getName() + "-signed-on", 10000);
                    if (signedOnObject != null) {
                        sendEchoTest();
                    } else {
                        sendSignOnRequest(m);
                    }
                } else {
                    space.in(mux.getName() + "-signed-on");
                }
            }
        }, 0, cfg.getLong("interval"));
    }

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
            long duration = System.currentTimeMillis() - start;
            log.info("Response time (ms):" + duration);
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

}
