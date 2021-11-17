package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.BalanceInquiryRequest;
import com.mii.komi.dto.inbound.BalanceInquiryResponse;
import com.mii.komi.dto.inbound.BaseInboundRequestDTO;
import com.mii.komi.service.ISO8583Service;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Utility;

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
public class BalanceInquiryParticipant implements TransactionParticipant, BaseInboundParticipant {
    
    public static String BALANCE_INQUIRY_PC = "310000";

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        BalanceInquiryRequest balanceInquiry = (BalanceInquiryRequest) ctx.get(Constants.HTTP_REQUEST);
        try {
            ISOMsg isoMsg = buildRequestMsg(balanceInquiry);
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
        BalanceInquiryRequest balanceInquiryRequest = (BalanceInquiryRequest) ctx.get(Constants.HTTP_REQUEST);
        ResponseEntity<BalanceInquiryResponse> restResponse = buildResponseMsg(balanceInquiryRequest, isoMsg);
        ctx.put(Constants.HTTP_RESPONSE, restResponse);
    }

    @Override
    public void abort(long id, Serializable context) {
        Context ctx = (Context) context;
        BalanceInquiryRequest balanceInquiryRequest = (BalanceInquiryRequest) ctx.get(Constants.HTTP_REQUEST);
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        ResponseEntity<BalanceInquiryResponse> rr = null;
        if (isoMsg == null) {
            rr = buildFailedResponseMsg(balanceInquiryRequest, null);
        } else {
            rr = buildFailedResponseMsg(balanceInquiryRequest, isoMsg);
        }
        ctx.put(Constants.HTTP_RESPONSE, rr);
    }

    @Override
    public ISOMsg buildRequestMsg(BaseInboundRequestDTO request) throws ISOException {
        BalanceInquiryRequest balanceInquiryRequest = (BalanceInquiryRequest) request;
        StringBuilder sb = new StringBuilder();
        String noRef = ISOUtil.strpad(balanceInquiryRequest.getNoRef(), 20);
        String accountNumber = ISOUtil.strpad(balanceInquiryRequest.getAccountNumber(), 34);
        sb.append(noRef).append(accountNumber);
        ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(BALANCE_INQUIRY_PC, request);
        isoMsg.set(48, sb.toString());
        return isoMsg;
    }

    @Override
    public ResponseEntity<BalanceInquiryResponse> buildFailedResponseMsg(BaseInboundRequestDTO request, ISOMsg isoMsg) {
        BalanceInquiryResponse rsp = new BalanceInquiryResponse();
        rsp.setNoRef(request.getNoRef());
        try {
            if (isoMsg != null) {
                if (isoMsg.hasField(62)) {
                    String privateData = isoMsg.getString(62);
                    int cursor = 0;
                    int endCursor = 20;
                    rsp.setNoRef(privateData.substring(cursor, endCursor));

                    cursor = endCursor;
                    endCursor = cursor + 4;
                    String rc = privateData.substring(cursor, endCursor);

                    cursor = endCursor;
                    endCursor = cursor + 35;
                    String rm = privateData.substring(cursor, endCursor);

                    rsp.setStatus(rc);
                    rsp.setReason(rm);
                } else {
                    rsp.setStatus(Constants.RESPONSE_CODE_REJECT);
                    rsp.setReason("U904");
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rsp);
            } else {
                rsp.setStatus(Constants.RESPONSE_CODE_KOMI_STATUS);
                rsp.setReason("K000");
                return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(rsp);
            }
        }catch (StringIndexOutOfBoundsException s){
            rsp.setStatus(Constants.RESPONSE_CODE_KOMI_STATUS);
            rsp.setReason("K000");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
        }
    }

    @Override
    public ResponseEntity<BalanceInquiryResponse> buildResponseMsg(BaseInboundRequestDTO request, ISOMsg isoRsp) {
        String privateData = isoRsp.getString(62);

        BalanceInquiryResponse rsp = new BalanceInquiryResponse();
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
        // Amount fix
        //rsp.setBalance(privateData.substring(cursor).trim());
        rsp.setBalance(Utility.getJSONMoney(privateData.substring(cursor).trim()));
        
        return ResponseEntity.ok(rsp);
    }
    
}
