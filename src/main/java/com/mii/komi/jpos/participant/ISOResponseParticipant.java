package com.mii.komi.jpos.participant;

import com.mii.komi.util.Constants;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.AbortParticipant;
import org.jpos.transaction.Context;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class ISOResponseParticipant implements AbortParticipant {

    @Override
    public int prepare(long id, Serializable context) {
        return PREPARED;
    }
    
    @Override
    public int prepareForAbort(long id, Serializable context) {
        return ABORTED;
    }

    @Override
    public void commit(long id, Serializable context) {
        sendResponse(id, context);
    }

    @Override
    public void abort(long id, Serializable context) {
        sendResponse(id, context);
    }

    private void sendResponse(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg rspMsg = (ISOMsg) ctx.get(Constants.ISO_RESPONSE);
        ISOSource sourceKey = (ISOSource) ctx.get(Constants.SOURCE_KEY);
        if (sourceKey != null) {
            try {
                if(sourceKey.isConnected()) {
                    sourceKey.send(rspMsg);
                } else {
                    System.out.println("Source is not connected, cannot send response");
                }
            } catch (ISOException ex) {
                Logger.getLogger(ISOResponseParticipant.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ISOResponseParticipant.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Space space = SpaceFactory.getSpace();
            String queueKey = rspMsg.getString(7) + rspMsg.getString(11);
            space.out(queueKey, context);
        }
    }

}
