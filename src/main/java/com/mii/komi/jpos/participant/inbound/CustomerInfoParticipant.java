package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.BaseInboundRequestDTO;
import com.mii.komi.dto.inbound.CustomerAccountInfoInboundRequest;
import com.mii.komi.dto.inbound.CustomerAccountInfoInboundResponse;
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
import static org.jpos.transaction.TransactionConstants.ABORTED;
import static org.jpos.transaction.TransactionConstants.PREPARED;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author vinch
 */
public class CustomerInfoParticipant implements TransactionParticipant, BaseInboundParticipant {

    public static String CUSTOMER_INFO_PC = "909200";

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        CustomerAccountInfoInboundRequest customerAccountInfo = (CustomerAccountInfoInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        try {
            ISOMsg isoMsg = buildRequestMsg(customerAccountInfo);
            ctx.put(Constants.ISO_REQUEST, isoMsg);
            return PREPARED;
        } catch (ISOException ex) {
            Logger.getLogger(AccountEnquiryInboundParticipant.class.getName()).log(Level.SEVERE, null, ex);
            return ABORTED;
        }
    }

    @Override
    public void commit(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        CustomerAccountInfoInboundRequest customerAccountInfoRequest = (CustomerAccountInfoInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        ResponseEntity<CustomerAccountInfoInboundResponse> restResponse = buildResponseMsg(customerAccountInfoRequest, isoMsg);
        ctx.put(Constants.HTTP_RESPONSE, restResponse);
    }

    @Override
    public void abort(long id, Serializable context) {
        Context ctx = (Context) context;
        CustomerAccountInfoInboundRequest customerAccountInfoRequest = (CustomerAccountInfoInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        ResponseEntity<CustomerAccountInfoInboundResponse> rr = null;
        if (isoMsg == null) {
            rr = buildFailedResponseMsg(customerAccountInfoRequest, null);
        } else {
            rr = buildFailedResponseMsg(customerAccountInfoRequest, isoMsg);
        }
        ctx.put(Constants.HTTP_RESPONSE, rr);
    }

    @Override
    public ISOMsg buildRequestMsg(BaseInboundRequestDTO request) throws ISOException {
        CustomerAccountInfoInboundRequest balanceInquiryRequest = (CustomerAccountInfoInboundRequest) request;
        StringBuilder sb = new StringBuilder();
        String noRef = ISOUtil.strpad(balanceInquiryRequest.getNoRef(), 20);
        String accountNumber = ISOUtil.strpad(balanceInquiryRequest.getAccountNumber(), 34);
        sb.append(noRef).append(accountNumber);
        ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(CUSTOMER_INFO_PC, request);
        isoMsg.set(48, sb.toString());
        return isoMsg;
    }

    @Override
    public ResponseEntity<CustomerAccountInfoInboundResponse> buildFailedResponseMsg(BaseInboundRequestDTO request, ISOMsg isoMsg) {
        CustomerAccountInfoInboundResponse rsp = new CustomerAccountInfoInboundResponse();
        rsp.setNoRef(request.getNoRef());
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

                rsp.setStatus(rc);
                rsp.setReason(rm);
            } else {
                rsp.setStatus(Constants.REASON_CODE_REJECT);
                rsp.setReason("U904");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rsp);
        } else {
            rsp.setStatus(Constants.REASON_CODE_KOMI_STATUS);
            rsp.setReason("K000");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(rsp);
        }
    }

    @Override
    public ResponseEntity<CustomerAccountInfoInboundResponse> buildResponseMsg(BaseInboundRequestDTO request, ISOMsg isoRsp) {
        String privateData = isoRsp.getString(62);
        String extPrivateData = isoRsp.getString(123);

        CustomerAccountInfoInboundResponse rsp = new CustomerAccountInfoInboundResponse();
        rsp.setTransactionId(request.getTransactionId());
        rsp.setDateTime(request.getDateTime());
        rsp.setMerchantType(isoRsp.getString(18));
        rsp.setTerminalId(isoRsp.getString(41));

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
        endCursor = cursor + 1;

        List<String> emailAddressList = new ArrayList<String>();
        int mailCount = Integer.parseInt(privateData.substring(cursor, endCursor));
        if (mailCount > 0) {
            for (int i = 0; i < mailCount; i++) {
                cursor = endCursor;
                endCursor = cursor + 50;
                emailAddressList.add(privateData.substring(cursor, endCursor));
            }
        }
        rsp.setEmailAddressList(emailAddressList);

        List<String> phoneNumberList = new ArrayList<String>();
        int phoneCount = Integer.parseInt(privateData.substring(cursor, endCursor));
        if (phoneCount > 0) {
            for (int i = 0; i < phoneCount; i++) {
                cursor = endCursor;
                endCursor = cursor + 50;
                phoneNumberList.add(privateData.substring(cursor, endCursor));
            }
        }
        rsp.setPhoneNumberList(phoneNumberList);
        
        cursor = 0;
        endCursor = 34;
        rsp.setAccountNumber(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setAccountType(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 140;
        rsp.setCustomerName(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setCustomerType(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setCustomerId(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        rsp.setCustomerIdType(extPrivateData.substring(cursor).trim());

        return ResponseEntity.ok(rsp);
    }

}
