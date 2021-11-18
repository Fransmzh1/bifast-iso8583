package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.BalanceInquiryRequest;
import com.mii.komi.dto.inbound.BalanceInquiryResponse;
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
public class InboundBalanceInquiryParticipant extends GenericInboundParticipantImpl {

    public static String BALANCE_INQUIRY_PC = "310000";

    Log log = Log.getLog("Q2", this.getClass().getName());

    @Override
    public ISOMsg buildRequestMsg(long id, Serializable context) {
        try {
            Context ctx = (Context) context;
            BalanceInquiryRequest balanceInquiry = (BalanceInquiryRequest) ctx.get(Constants.HTTP_REQUEST);
            StringBuilder sb = new StringBuilder();
            String noRef = ISOUtil.strpad(balanceInquiry.getNoRef(), 20);
            String accountNumber = ISOUtil.strpad(balanceInquiry.getAccountNumber(), 34);
            sb.append(noRef).append(accountNumber);
            ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(BALANCE_INQUIRY_PC, balanceInquiry);
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
        ISOMsg isoRsp = ctx.get(Constants.ISO_RESPONSE);
        BalanceInquiryRequest request = (BalanceInquiryRequest) ctx.get(Constants.HTTP_REQUEST);
        BalanceInquiryResponse rsp = new BalanceInquiryResponse();
        
        rsp.setTransactionId(request.getTransactionId());
        rsp.setDateTime(request.getDateTime());
        rsp.setMerchantType(isoRsp.getString(18));
        rsp.setTerminalId(isoRsp.getString(41));
        String privateData = isoRsp.getString(62);
        
        int cursor = 0;
        int endCursor = 20;
        rsp.setNoRef(privateData.substring(cursor, endCursor).trim());

        cursor = endCursor;
        endCursor = cursor + 4;
        rsp.setStatus(privateData.substring(cursor, endCursor).trim());

        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setReason(privateData.substring(cursor, endCursor).trim());

        cursor = endCursor;
        rsp.setBalance(Utility.getJSONMoney(privateData.substring(cursor).trim()));
        
        return ResponseEntity.ok(rsp);
    }

}
