package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.BaseInboundRequestDTO;
import com.mii.komi.dto.inbound.SettlementRequest;
import com.mii.komi.dto.inbound.SettlementResponse;
import com.mii.komi.service.ISO8583Service;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import static org.jpos.transaction.TransactionConstants.ABORTED;
import static org.jpos.transaction.TransactionConstants.PREPARED;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author vinch
 */
public class SettlementParticipant implements TransactionParticipant, BaseInboundParticipant {

    public static String SETTLEMENT_CONFIRMATION_PC = "489100";

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        SettlementRequest settlementRequest = (SettlementRequest) ctx.get(Constants.HTTP_REQUEST);
        try {
            ISOMsg isoMsg = buildRequestMsg(settlementRequest);
            ctx.put(Constants.ISO_REQUEST, isoMsg);
            return PREPARED;
        } catch (ISOException ex) {
            Logger.getLogger(AccountEnquiryInboundParticipant.class.getName()).log(Level.SEVERE, null, ex);
            return ABORTED;
        }
    }

    @Override
    public void abort(long id, Serializable context) {
        Context ctx = (Context) context;
        SettlementRequest settlementRequest = (SettlementRequest) ctx.get(Constants.HTTP_REQUEST);
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        ResponseEntity<SettlementResponse> rr = null;
        if (isoMsg == null) {
            rr = buildFailedResponseMsg(settlementRequest, null);
        } else {
            rr = buildFailedResponseMsg(settlementRequest, isoMsg);
        }
        ctx.put(Constants.HTTP_RESPONSE, rr);
    }

    @Override
    public void commit(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        SettlementRequest settlementRequest = (SettlementRequest) ctx.get(Constants.HTTP_REQUEST);
        ResponseEntity<SettlementResponse> restResponse = buildResponseMsg(settlementRequest, isoMsg);
        ctx.put(Constants.HTTP_RESPONSE, restResponse);
    }

    @Override
    public ISOMsg buildRequestMsg(BaseInboundRequestDTO request) throws ISOException {
        SettlementRequest settlementRequest = (SettlementRequest) request;
        StringBuilder sb = new StringBuilder();
        String noRef = ISOUtil.strpad(settlementRequest.getNoRef(), 20);
        String originalNoRef = ISOUtil.strpad(settlementRequest.getOriginalNoRef(), 20);
        String status = ISOUtil.strpad(settlementRequest.getStatus(), 4);
        String reason = ISOUtil.zeropad(String.valueOf(settlementRequest.getReason()), 35);
        String additionalInfo = ISOUtil.zeropad(settlementRequest.getAdditionalInfo(), 140);
        sb.append(noRef).append(originalNoRef).append(status).append(reason).append(additionalInfo);
        ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(SETTLEMENT_CONFIRMATION_PC, request);
        isoMsg.set(48, sb.toString());
        return isoMsg;
    }

    @Override
    public ResponseEntity<SettlementResponse> buildResponseMsg(BaseInboundRequestDTO request, ISOMsg isoMsg) {
        SettlementResponse rsp = new SettlementResponse();
        rsp.setTransactionId(request.getTransactionId());
        rsp.setDateTime(request.getDateTime());
        rsp.setMerchantType(isoMsg.getString(18));
        rsp.setTerminalId(isoMsg.getString(41));
        rsp.setNoRef(request.getNoRef());
        rsp.setStatus(Constants.RESPONSE_CODE_ACCEPTED);
        return ResponseEntity.ok(rsp);
    }

    @Override
    public ResponseEntity<SettlementResponse> buildFailedResponseMsg(BaseInboundRequestDTO req, ISOMsg isoMsg) {
        SettlementResponse rsp = new SettlementResponse();
        rsp.setNoRef(req.getNoRef());
        if (isoMsg != null) {
            rsp.setStatus(Constants.RESPONSE_CODE_REJECT);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rsp);
        } else {
            rsp.setStatus(Constants.RESPONSE_CODE_KOMI_STATUS);
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(rsp);
        }
    }

}
