package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.RestResponse;
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
import org.jpos.transaction.TransactionParticipant;
import org.springframework.http.ResponseEntity;

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
        ResponseEntity httpRsp = ctx.get(Constants.HTTP_RESPONSE);
        ISOMsg rsp = buildResponseMsg(req, httpRsp);
        ctx.put(Constants.ISO_RESPONSE, rsp);
    }

    @Override
    public void abort(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg req = ctx.get(Constants.ISO_REQUEST);
        ResponseEntity httpRsp = ctx.get(Constants.HTTP_RESPONSE);
        ISOMsg rsp = buildFailedResponseMsg(req, httpRsp);
        ctx.put(Constants.ISO_RESPONSE, rsp);
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr) {
        ISOMsg isoRsp = (ISOMsg) req.clone();
        isoRsp.set(39, Constants.ISO_RSP_REJECTED);
        try {
            isoRsp.setResponseMTI();
            return isoRsp;
        } catch (ISOException ex) {
            Logger.getLogger(OutboundParticipant.class.getName()).log(Level.SEVERE, null, ex);
            return isoRsp;
        }
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> dto) {
        ISOMsg isoRsp = (ISOMsg) req.clone();
        if (dto.hasBody()) {
            String responseCode = dto.getBody().getResponseCode();
            if (Constants.RESPONSE_CODE_ACCEPTED.equals(responseCode)) {
                isoRsp.set(39, Constants.ISO_RSP_APPROVED);
            } else {
                isoRsp.set(39, Constants.ISO_RSP_REJECTED);
            }
            try {
                isoRsp.setResponseMTI();
                return isoRsp;
            } catch (ISOException ex) {
                Logger.getLogger(OutboundParticipant.class.getName()).log(Level.SEVERE, null, ex);
                return isoRsp;
            }
        } else {
            isoRsp.set(39, Constants.ISO_RSP_REJECTED);
            return isoRsp;
        }
    }

    @Override
    public void setConfiguration(Configuration c) throws ConfigurationException {
        this.cfg = c;
    }

}
