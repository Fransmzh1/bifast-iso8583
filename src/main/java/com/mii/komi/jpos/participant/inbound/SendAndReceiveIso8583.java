package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.service.ISO8583Service;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import static org.jpos.transaction.TransactionConstants.NO_JOIN;
import static org.jpos.transaction.TransactionConstants.PREPARED;
import org.jpos.transaction.TransactionParticipant;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class SendAndReceiveIso8583 implements TransactionParticipant, Configurable {

    private Configuration cfg;
    
    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            ISOMsg rsp = ISO8583Service.sendMessage(cfg.get("destination"), reqMsg);
            ctx.put(Constants.ISO_RESPONSE, rsp);
            if(rsp == null) {
                return ABORTED | NO_JOIN;
            } else {
                if(Constants.ISO_RSP_APPROVED.equals(rsp.getString(39))) {
                    return PREPARED | NO_JOIN;
                } else {
                    return ABORTED | NO_JOIN;
                }
            }
        } catch (ISOException ex) {
            Logger.getLogger(SendAndReceiveIso8583.class.getName()).log(Level.SEVERE, null, ex);
            return ABORTED | NO_JOIN;
        }
    }

    @Override
    public void setConfiguration(Configuration c) throws ConfigurationException {
        this.cfg = c;
    }

}
