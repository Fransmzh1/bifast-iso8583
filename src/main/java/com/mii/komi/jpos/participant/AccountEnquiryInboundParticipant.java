package com.mii.komi.jpos.participant;

import com.mii.komi.dto.AccountEnquiryRequest;
import com.mii.komi.dto.AccountEnquiryResponse;
import com.mii.komi.dto.BaseRequestDTO;
import com.mii.komi.dto.RestResponse;
import com.mii.komi.dto.RootAccountEnquiryRequest;
import com.mii.komi.service.ISO8583Service;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class AccountEnquiryInboundParticipant implements TransactionParticipant, BaseInboundParticipant {

    public static String ACCOUNT_ENQUIRY_INBOUND_PC = "389100";
    
    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        RootAccountEnquiryRequest accountEnquiryRequest = (RootAccountEnquiryRequest) ctx.get(Constants.HTTP_REQUEST);
        try {
            ISOMsg isoMsg = buildRequestMsg(accountEnquiryRequest.getAccountEnquiryRequest());
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
        AccountEnquiryRequest accountEnquiryRequest = (AccountEnquiryRequest) ctx.get(Constants.HTTP_REQUEST);
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        ResponseEntity<RestResponse<AccountEnquiryResponse>> rr = null;
        if (isoMsg == null) {
            rr = buildFailedResponseMsg(accountEnquiryRequest, null);
        } else {
            rr = buildFailedResponseMsg(accountEnquiryRequest, isoMsg);
        }
        ctx.put(Constants.HTTP_RESPONSE, rr);
    }

    @Override
    public void commit(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        ResponseEntity<RestResponse<AccountEnquiryResponse>> restResponse = buildResponseMsg(isoMsg);
        ctx.put(Constants.HTTP_RESPONSE, restResponse);
    }

    @Override
    public ISOMsg buildRequestMsg(BaseRequestDTO request) throws ISOException {
        AccountEnquiryRequest accountEnquiryRequest = (AccountEnquiryRequest) request;
        StringBuilder sb = new StringBuilder();
        String noRef = ISOUtil.strpad(accountEnquiryRequest.getNoRef(), 20);
        String recipientBank = ISOUtil.strpad(accountEnquiryRequest.getRecipientBank(), 35);
        String amount = ISOUtil.zeropad(String.valueOf(accountEnquiryRequest.getAmount()), 18);
        String categoryPurpose = ISOUtil.zeropad(accountEnquiryRequest.getCategoryPurpose(), 2);
        String accountNumber = ISOUtil.strpad(accountEnquiryRequest.getAccountNumber(), 34);
        sb.append(noRef).append(recipientBank).append(amount).append(categoryPurpose).append(accountNumber);

        ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(ACCOUNT_ENQUIRY_INBOUND_PC, noRef, amount);
        isoMsg.set(48, sb.toString());
        return isoMsg;
    }

    @Override
    public ResponseEntity<RestResponse<AccountEnquiryResponse>> buildResponseMsg(ISOMsg isoMsg) {
        String privateData = isoMsg.getString(62);

        AccountEnquiryResponse rsp = new AccountEnquiryResponse();
        int cursor = 0;
        int endCursor = 20;
        rsp.setNoRef(privateData.substring(cursor, endCursor).trim());

        cursor = endCursor;
        endCursor = cursor + 4;
        String rc = privateData.substring(cursor, endCursor).trim();

        cursor = endCursor;
        endCursor = cursor + 35;
        String rm = privateData.substring(cursor, endCursor).trim();

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

        List<AccountEnquiryResponse> list = new ArrayList<>();
        list.add(rsp);
        return ResponseEntity.ok(RestResponse.success(rm, list));
    }

    @Override
    public ResponseEntity<RestResponse<AccountEnquiryResponse>> buildFailedResponseMsg(BaseRequestDTO req, ISOMsg isoMsg) {
        AccountEnquiryResponse rsp = new AccountEnquiryResponse();
        List<AccountEnquiryResponse> list = new ArrayList<>();
        
        AccountEnquiryRequest originalRequest = (AccountEnquiryRequest) req;
        rsp.setNoRef(req.getNoRef());
        rsp.setAccountNumber(originalRequest.getAccountNumber());
        RestResponse rr = null;
        if (isoMsg != null) {
            if (isoMsg.hasField(62)) {
                String privateData = isoMsg.getString(62);
                int cursor = 0;
                int endCursor = 20;
                rsp.setNoRef(privateData.substring(cursor, endCursor));

                cursor = cursor + endCursor;
                endCursor = cursor + 4;
                String rc = privateData.substring(cursor, endCursor);

                cursor = cursor + endCursor;
                endCursor = cursor + 35;
                String rm = privateData.substring(cursor, endCursor);

                cursor = cursor + endCursor;
                endCursor = cursor + 34;
                rsp.setAccountNumber(privateData.substring(cursor, endCursor));

                cursor = cursor + endCursor;
                endCursor = cursor + 35;
                rsp.setAccountType(privateData.substring(cursor, endCursor));
                rr = RestResponse.failed(rm, rm, rc);
            } else {
                rr = RestResponse.failed(Constants.REASON_CODE_REJECT, "Error Undefined" , "U904");
            }
            list.add(rsp);
            rr.setContent(list);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rr);
        } else {
            rr = RestResponse.failed(Constants.REASON_CODE_KOMI_STATUS, "Internal Timeout", "K000");
            list.add(rsp);
            rr.setContent(list);
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(rr);
        }
    }

}
