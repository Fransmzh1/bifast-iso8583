package com.mii.komi.jpos.participant;

import com.mii.komi.util.Constants;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

/**
 *
 * @author vinch
 */
public class NetworkManagementParticipant implements TransactionParticipant{

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context)context;
        ISOMsg reqMsg = (ISOMsg)ctx.get(Constants.ISO_REQUEST);
        try {
            ISOMsg rspMsg = (ISOMsg) reqMsg.clone();
            rspMsg.setDirection(ISOMsg.OUTGOING);
            rspMsg.setResponseMTI();
            rspMsg.set(39, "00");
            ctx.put(Constants.ISO_RESPONSE, rspMsg);
        } catch (ISOException ex) {
            Logger.getLogger(NetworkManagementParticipant.class.getName()).log(Level.SEVERE, null, ex);
        }
        return PREPARED;
    }
    
}
