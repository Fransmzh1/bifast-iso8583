package org.jpos.iso.channel;

import java.io.IOException;
import org.jpos.iso.ISOException;
import org.jpos.iso.channel.NACChannel;
import org.jpos.util.Log;

/**
 *
 * @author vinch
 */
public class WBKChannel extends NACChannel {
    
    Log log = Log.getLog("Q2", "wbk-channel");
    
    protected void sendMessageLength(int len) throws IOException {
        super.sendMessageLength(len);
        if(getHost() != null) {
            log.info("Send Message Length to " +getHost()+ " : " + len);
        } else {
            log.info("Send Message Length : " + len);
        }
    }
    
    protected int getMessageLength() throws IOException, ISOException {
        int len = super.getMessageLength();
        if(getHost() != null) {
            log.info("Received Message Length " + getHost()+ " : " + len);
        } else {
            log.info("Received Message Length : " + len);
        }
        return len;
    }
    
}
