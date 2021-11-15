package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.AbortParticipant;
import org.jpos.transaction.Context;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RESTResponseParticipant implements AbortParticipant {

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
        String queueKey = ctx.get(Constants.QUEUE_KEY);
        Space space = SpaceFactory.getSpace();
        space.out(queueKey, ctx);
    }

}
