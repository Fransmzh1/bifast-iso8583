package org.jpos.iso.channel;

import java.io.IOException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.NACChannel;
import org.jpos.util.Log;

/**
 *
 * @author vinch
 */
public class WBKChannel extends NACChannel {
    
    Log log = Log.getLog("Q2", "wbk-channel");
    
    @Override
    protected void sendMessageLength(int len) throws IOException {
        /*
        len += lenlen;
        int sentLent1 = len >> 8; // high byte
        serverOut.write (sentLent1);
        int sentLent2 = len & 0xFF; // low byte
        serverOut.write (sentLent2);
        if(getHost() != null) {
            log.info("Send Hexa Message Length to " +getHost()+ " : " + Integer.toHexString(sentLent1).toUpperCase() + "" + Integer.toHexString(sentLent2).toUpperCase());
        } else {
            log.info("Send Hexa Message Length : " + Integer.toHexString(sentLent1).toUpperCase() + "" + Integer.toHexString(sentLent2).toUpperCase());
        }
        */
        byte[] bb = new byte[2];
        len += lenlen;
        bb[0] = (byte) (len >> 8);
        bb[1] = (byte) (len & 0xFF);
        serverOut.write(bb);
        if(getHost() != null) {
            log.info("Send Hexa Message Length to " + getHost()+ " : " + ISOUtil.hexString(bb));
        } else {
            log.info("Send Hexa Message Length : " + ISOUtil.hexString(bb));
        }
    }
    
    @Override
    protected int getMessageLength() throws IOException, ISOException {
        byte[] b = new byte[2];
        serverIn.readFully(b,0,2);
        if(getHost() != null) {
            log.info("Received Hexa Message Length " + getHost()+ " : " + ISOUtil.hexString(b));
        } else {
            log.info("Received Hexa Message Length : " + ISOUtil.hexString(b));
        }
        return (((int)b[0] &0xFF) << 8 | (int)b[1] &0xFF) - lenlen;
    }
    
}
