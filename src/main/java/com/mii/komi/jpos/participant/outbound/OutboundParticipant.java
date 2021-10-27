package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

/**
 *
 * @author vinch
 */
public abstract class OutboundParticipant implements TransactionParticipant, BaseOutboundParticipant, Configurable {

    protected Configuration cfg;
    
    @Override
    public void commit(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg req = ctx.get(Constants.ISO_REQUEST);
        RestResponse httpRsp = ctx.get(Constants.HTTP_RESPONSE);
        try {
            ISOMsg rsp = buildResponseMsg(req, httpRsp);
            ctx.put(Constants.ISO_RESPONSE, rsp);
        } catch (ISOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void abort(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg req = ctx.get(Constants.ISO_REQUEST);
        RestResponse httpRsp = ctx.get(Constants.HTTP_RESPONSE);
        ISOMsg rsp = buildFailedResponseMsg(req, httpRsp);
        ctx.put(Constants.ISO_RESPONSE, rsp);
    }

    @Override
    public void setConfiguration(Configuration c) throws ConfigurationException {
        this.cfg = c;
    }
    
}
