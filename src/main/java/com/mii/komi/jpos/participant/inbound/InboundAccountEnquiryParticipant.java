package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.AccountEnquiryInboundRequest;
import com.mii.komi.dto.inbound.AccountEnquiryInboundResponse;
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
public class InboundAccountEnquiryParticipant extends GenericInboundParticipantImpl {

    public static String ACCOUNT_ENQUIRY_INBOUND_PC = "389100";

    Log log = Log.getLog("Q2", this.getClass().getName());

    @Override
    public ISOMsg buildRequestMsg(long id, Serializable context) {
        try {
            Context ctx = (Context) context;
            AccountEnquiryInboundRequest accountEnquiryRequest = ctx.get(Constants.HTTP_REQUEST);
            StringBuilder sb = new StringBuilder();
            String noRef = ISOUtil.strpad(accountEnquiryRequest.getNoRef(), 20);
            String recipientBank = ISOUtil.strpad(accountEnquiryRequest.getRecipientBank(), 35);

            // fix amount
            String amount = Utility.getISOMoney(accountEnquiryRequest.getAmount());

            String categoryPurpose = ISOUtil.zeropad(accountEnquiryRequest.getCategoryPurpose(), 2);
            String accountNumber = ISOUtil.strpad(accountEnquiryRequest.getAccountNumber(), 34);
            sb.append(noRef).append(recipientBank).append(amount).append(categoryPurpose).append(accountNumber);
            ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(ACCOUNT_ENQUIRY_INBOUND_PC, accountEnquiryRequest);
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
        AccountEnquiryInboundRequest request = (AccountEnquiryInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        AccountEnquiryInboundResponse rsp = new AccountEnquiryInboundResponse();
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

        if (Constants.ISO_RSP_APPROVED.equals(isoRsp.getString(39))) {
            cursor = endCursor;
            endCursor = cursor + 34;
            rsp.setAccountNumber(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            rsp.setAccountType(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 140;
            rsp.setCreditorName(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            rsp.setCreditorId(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            rsp.setCreditorType(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            rsp.setResidentStatus(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            rsp.setTownName(privateData.substring(cursor).trim());
        }else if(Constants.ISO_RSP_REJECTED.equals(isoRsp.getString(39))){
            cursor = endCursor;
            endCursor = cursor + 34;
            if(!privateData.substring(cursor, endCursor).trim().isEmpty()){
                rsp.setAccountNumber(privateData.substring(cursor, endCursor).trim());
            }
        }
        return ResponseEntity.ok(rsp);
    }

}
