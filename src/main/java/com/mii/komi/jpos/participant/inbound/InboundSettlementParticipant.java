package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.SettlementRequest;
import com.mii.komi.dto.inbound.SettlementResponse;
import com.mii.komi.service.ISO8583Service;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Utility;
import java.io.Serializable;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import org.jpos.util.Log;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author vinch
 */
public class InboundSettlementParticipant extends GenericInboundParticipantImpl {

    public static String SETTLEMENT_CONFIRMATION_PC = "489100";

    Log log = Log.getLog("Q2", this.getClass().getName());

    @Override
    public ISOMsg buildRequestMsg(long id, Serializable context) {
        try {
            Context ctx = (Context) context;
            SettlementRequest settlementRequest = (SettlementRequest) ctx.get(Constants.HTTP_REQUEST);

            StringBuilder sb = new StringBuilder();
            String noRef = ISOUtil.strpad(settlementRequest.getNoRef(), 20);
            String originalNoRef = ISOUtil.strpad(settlementRequest.getOriginalNoRef(), 20);
            String status = ISOUtil.strpad(settlementRequest.getStatus(), 4);
            String reason = ISOUtil.zeropad(String.valueOf(settlementRequest.getReason()), 35);
            String additionalInfo = ISOUtil.zeropad(settlementRequest.getAdditionalInfo(), 140);
            sb.append(noRef).append(originalNoRef).append(status).append(reason).append(additionalInfo);
            ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(SETTLEMENT_CONFIRMATION_PC, settlementRequest);
            isoMsg.set(48, sb.toString());

            return isoMsg;
        } catch (ISOException ex) {
            log.error(Utility.getExceptionStackTraceAsString(ex));
            return null;
        }
    }

    @Override
    public ResponseEntity buildSpesificRspBody(long id, Serializable context) {
        Context ctx = (Context) context;
        SettlementRequest request = (SettlementRequest) ctx.get(Constants.HTTP_REQUEST);
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        
        SettlementResponse rsp = new SettlementResponse();
        rsp.setTransactionId(request.getTransactionId());
        rsp.setDateTime(request.getDateTime());
        rsp.setMerchantType(isoMsg.getString(18));
        rsp.setTerminalId(isoMsg.getString(41));
        rsp.setNoRef(request.getNoRef());
        rsp.setStatus(Constants.RESPONSE_CODE_ACCEPTED);
        return ResponseEntity.ok(rsp);
    }

}
