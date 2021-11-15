package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.BaseInboundRequestDTO;
import com.mii.komi.dto.inbound.DebitTransferInboundRequest;
import com.mii.komi.dto.inbound.DebitTransferInboundResponse;
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
 * @author Erwin Sugianto Santoso - MII
 */
public class DebitTransferParticipant implements TransactionParticipant, BaseInboundParticipant {

    public static String DEBIT_TRANSFER_PC = "109000";

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        DebitTransferInboundRequest debitTransferRequest = (DebitTransferInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        try {
            ISOMsg isoMsg = buildRequestMsg(debitTransferRequest);
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
        DebitTransferInboundRequest debitTransferInboundRequest = (DebitTransferInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        ResponseEntity<DebitTransferInboundResponse> restResponse = buildResponseMsg(debitTransferInboundRequest, isoMsg);
        ctx.put(Constants.HTTP_RESPONSE, restResponse);
    }

    @Override
    public void abort(long id, Serializable context) {
        Context ctx = (Context) context;
        DebitTransferInboundRequest accountEnquiryRequest = (DebitTransferInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        ResponseEntity<DebitTransferInboundResponse> rr = null;
        if (isoMsg == null) {
            rr = buildFailedResponseMsg(accountEnquiryRequest, null);
        } else {
            rr = buildFailedResponseMsg(accountEnquiryRequest, isoMsg);
        }
        ctx.put(Constants.HTTP_RESPONSE, rr);
    }

    @Override
    public ISOMsg buildRequestMsg(BaseInboundRequestDTO request) throws ISOException {
        DebitTransferInboundRequest debitRequest = (DebitTransferInboundRequest) request;

        StringBuilder content48 = new StringBuilder();
        String noRef = ISOUtil.strpad(debitRequest.getNoRef(), 20);
        String originalNoRef = ISOUtil.strpad("", 20);
        String originalDateTime = ISOUtil.strpad("0000000000", 10);
        String categoryPurpose = ISOUtil.zeropad(debitRequest.getCategoryPurpose(), 2);
        String debtorName = ISOUtil.strpad(debitRequest.getDebtorName(), 140);
        String debtorType = ISOUtil.strpad(debitRequest.getDebtorType(), 35);
        String debtorId = ISOUtil.strpad(debitRequest.getDebtorId(), 35);
        String debtorAccountNumber = ISOUtil.strpad(debitRequest.getDebtorAccountNumber(), 34);
        String debtorAccountType = ISOUtil.strpad(debitRequest.getDebtorAccountType(), 35);
        String debtorResidentStatus = ISOUtil.strpad(debitRequest.getDebtorResidentStatus(), 35);
        String debtorTownName = ISOUtil.strpad(debitRequest.getDebtorTownName(), 35);

        // Amount fix
        //String amount = ISOUtil.zeropad(String.valueOf(debitRequest.getAmount()), 18);
        //String feeTransfer = ISOUtil.zeropad(String.valueOf(debitRequest.getAmount()), 18);
        String amount = Utility.getISOMoney(debitRequest.getAmount());
        String feeTransfer = Utility.getISOMoney(debitRequest.getFeeTransfer());
        
        content48.append(noRef).append(originalNoRef).append(originalDateTime).append(categoryPurpose)
                .append(debtorName).append(debtorType).append(debtorId).append(debtorAccountNumber)
                .append(debtorAccountNumber).append(debtorAccountType).append(debtorResidentStatus)
                .append(debtorTownName).append(amount).append(feeTransfer);

        StringBuilder content123 = new StringBuilder();
        String recipientBank = ISOUtil.strpad(debitRequest.getRecipientBank(), 35);
        String creditorName = ISOUtil.strpad(debitRequest.getCreditorName(), 140);
        String creditorType = ISOUtil.strpad(debitRequest.getCreditorType(), 35);
        String creditorId = ISOUtil.strpad(debitRequest.getCreditorId(), 35);
        String creditorAccountNumber = ISOUtil.strpad(debitRequest.getCreditorAccountNumber(), 34);
        String creditorAccountType = ISOUtil.strpad(debitRequest.getCreditorAccountType(), 35);
        String creditorResidentStatus = ISOUtil.strpad(debitRequest.getCreditorResidentStatus(), 35);
        String creditorTownName = ISOUtil.strpad(debitRequest.getCreditorTownName(), 35);
        String creditorProxyId = ISOUtil.strpad(debitRequest.getCreditorProxyId(), 140);
        String creditorProxyType = ISOUtil.strpad(debitRequest.getCreditorProxyType(), 35);
        String paymentInformation = ISOUtil.strpad(debitRequest.getPaymentInformation(), 140);
        content123.append(recipientBank).append(creditorName).append(creditorType).append(creditorId)
                .append(creditorAccountNumber).append(creditorAccountType).append(creditorResidentStatus)
                .append(creditorTownName).append(creditorProxyId).append(creditorProxyType).append(paymentInformation);

        ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(DEBIT_TRANSFER_PC, request);
        isoMsg.set(48, content48.toString());
        isoMsg.set(123, content123.toString());

        return isoMsg;
    }

    @Override
    public ResponseEntity buildFailedResponseMsg(BaseInboundRequestDTO request, ISOMsg rsp) {
        DebitTransferInboundResponse debitTransferRsp = new DebitTransferInboundResponse();
        DebitTransferInboundRequest originalRequest = (DebitTransferInboundRequest) request;
        debitTransferRsp.setNoRef(request.getNoRef());
        debitTransferRsp.setAccountNumber(originalRequest.getCreditorAccountNumber());
        if (rsp != null) {
            if (rsp.hasField(62)) {
                String privateData = rsp.getString(62);
                int cursor = 0;
                int endCursor = 20;
                debitTransferRsp.setNoRef(privateData.substring(cursor, endCursor));

                cursor = endCursor;
                endCursor = cursor + 4;
                String rc = privateData.substring(cursor, endCursor);

                cursor = endCursor;
                endCursor = cursor + 35;
                String rm = privateData.substring(cursor, endCursor);

                cursor = endCursor;
                endCursor = cursor + 34;
                debitTransferRsp.setAccountNumber(privateData.substring(cursor, endCursor));
                
                debitTransferRsp.setStatus(rc);
                debitTransferRsp.setReason(rm);
            } else {
                debitTransferRsp.setStatus(Constants.RESPONSE_CODE_REJECT);
                debitTransferRsp.setReason("U904");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rsp);
        } else {
            debitTransferRsp.setStatus(Constants.RESPONSE_CODE_KOMI_STATUS);
            debitTransferRsp.setReason("K000");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(rsp);
        }
    }

    @Override
    public ResponseEntity<DebitTransferInboundResponse> buildResponseMsg(BaseInboundRequestDTO request, ISOMsg isoMsg) {
        String privateData = isoMsg.getString(62);

        DebitTransferInboundResponse rsp = new DebitTransferInboundResponse();
        rsp.setTransactionId(request.getTransactionId());
        rsp.setDateTime(request.getDateTime());
        rsp.setMerchantType(isoMsg.getString(18));
        rsp.setTerminalId(isoMsg.getString(41));

        int cursor = 0;
        int endCursor = 20;
        rsp.setNoRef(privateData.substring(cursor, endCursor).trim());

        cursor = endCursor;
        endCursor = cursor + 4;
        rsp.setStatus(privateData.substring(cursor, endCursor).trim());

        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setReason(privateData.substring(cursor, endCursor).trim());

        if (privateData.length() > 59) {
            cursor = endCursor;
            endCursor = cursor + 140;
            rsp.setAdditionalInfo(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            rsp.setAccountNumber(privateData.substring(cursor).trim());
        }

        return ResponseEntity.ok(rsp);
    }

}
