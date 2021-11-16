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
        len += lenlen;
        int sentLent1 = len >> 8;
        serverOut.write (sentLent1);
        int sentLent2 = len;
        serverOut.write (sentLent2);
        if(getHost() != null) {
            log.info("Send Hexa Message Length to " +getHost()+ " : " + Integer.toHexString(sentLent1) + "" + Integer.toHexString(sentLent2));
        } else {
            log.info("Send Hexa Message Length : " + Integer.toHexString(sentLent1) + "" + Integer.toHexString(sentLent2));
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
