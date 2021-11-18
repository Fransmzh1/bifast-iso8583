package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.BaseInboundRequestDTO;
import com.mii.komi.dto.inbound.BaseInboundResponseDTO;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author vinch
 */
public abstract class GenericInboundParticipantImpl implements BaseInboundParticipant, TransactionParticipant {
    
    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg isoMsg = buildRequestMsg(id, context);
        ctx.put(Constants.ISO_REQUEST, isoMsg);
        return PREPARED;
    }
    
    @Override
    public void commit(long id, Serializable context) {
        Context ctx = (Context) context;
        ctx.put(Constants.HTTP_RESPONSE, buildResponseMsg(id, context));
    }
    
    @Override
    public void abort(long id, Serializable context) {
        Context ctx = (Context) context;
        ctx.put(Constants.HTTP_RESPONSE, buildFailedResponseMsg(id, context));
    }
    
    @Override
    public ResponseEntity buildResponseMsg(long id, Serializable context) {
        Context ctx = (Context) context;
        String status = ctx.get(Constants.STATUS);
        BaseInboundRequestDTO request = ctx.get(Constants.HTTP_REQUEST);
        BaseInboundResponseDTO rsp = new BaseInboundResponseDTO();
        rsp.setTransactionId(request.getTransactionId());
        rsp.setDateTime(request.getDateTime());
        if (Constants.STATUS_RESPONSE_ACCEPTED.equals(status)) {
            return buildSpesificRspBody(id, context);
        } else if (Constants.STATUS_NOT_CONNECTED.equals(status)) {
            rsp.setTransactionId(request.getTransactionId());
            rsp.setDateTime(request.getDateTime());
            rsp.setMerchantType(request.getMerchantType());
            rsp.setTerminalId(request.getTerminalId());
            rsp.setNoRef(request.getNoRef());
            rsp.setStatus(Constants.RESPONSE_CODE_REJECT);
            rsp.setReason(Constants.REASON_CODE_OTHER);
            return ResponseEntity.ok(rsp);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity buildFailedResponseMsg(long id, Serializable context) {
        Context ctx = (Context) context;
        BaseInboundRequestDTO req = ctx.get(Constants.HTTP_REQUEST);
        String status = ctx.get(Constants.STATUS);
        BaseInboundResponseDTO rsp = new BaseInboundResponseDTO();
        if (Constants.STATUS_NO_RESPONSE.equals(status) || (Constants.STATUS_BAD_RESPONSE.equals(status))) {
            rsp.setNoRef(req.getNoRef());
            rsp.setDateTime(req.getDateTime());
            rsp.setTerminalId(req.getTerminalId());
            rsp.setTransactionId(req.getTransactionId());
            rsp.setMerchantType(req.getMerchantType());
            rsp.setStatus(Constants.RESPONSE_CODE_KOMI_STATUS);
            rsp.setReason(Constants.REASON_CODE_UNDEFINED);
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(rsp);
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

}
