package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.BaseInboundRequestDTO;
import com.mii.komi.dto.inbound.DebitReversalInboundRequest;
import com.mii.komi.dto.inbound.DebitReversalInboundResponse;
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
public class DebitReversalParticipant implements TransactionParticipant, BaseInboundParticipant {

    public static String DEBIT_REVERSAL_TRANSFER_PC = "109100";

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        DebitReversalInboundRequest debitReversalRequest = (DebitReversalInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        try {
            ISOMsg isoMsg = buildRequestMsg(debitReversalRequest);
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
        DebitReversalInboundResponse debitReversalInboundRequest = (DebitReversalInboundResponse) ctx.get(Constants.HTTP_REQUEST);
        ResponseEntity<DebitReversalInboundResponse> restResponse = buildResponseMsg(debitReversalInboundRequest, isoMsg);
        ctx.put(Constants.HTTP_RESPONSE, restResponse);
    }

    @Override
    public void abort(long id, Serializable context) {
        Context ctx = (Context) context;
        DebitReversalInboundRequest accountEnquiryRequest = (DebitReversalInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);
        ResponseEntity<DebitReversalInboundResponse> rr = null;
        if (isoMsg == null) {
            rr = buildFailedResponseMsg(accountEnquiryRequest, null);
        } else {
            rr = buildFailedResponseMsg(accountEnquiryRequest, isoMsg);
        }
        ctx.put(Constants.HTTP_RESPONSE, rr);
    }

    @Override
    public ISOMsg buildRequestMsg(BaseInboundRequestDTO request) throws ISOException {
        DebitReversalInboundRequest debitRequest = (DebitReversalInboundRequest) request;

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
        String amount = ISOUtil.zeropad(String.valueOf(debitRequest.getAmount()), 18);
        String feeTransfer = ISOUtil.zeropad(String.valueOf(debitRequest.getAmount()), 18);
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

        ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(DEBIT_REVERSAL_TRANSFER_PC, request);
        isoMsg.set(48, content48.toString());
        isoMsg.set(123, content123.toString());

        return isoMsg;
    }

    @Override
    public ResponseEntity buildFailedResponseMsg(BaseInboundRequestDTO request, ISOMsg rsp) {
        DebitReversalInboundResponse debitTransferRsp = new DebitReversalInboundResponse();
        DebitReversalInboundRequest originalRequest = (DebitReversalInboundRequest) request;
        debitTransferRsp.setNoRef(request.getNoRef());
        debitTransferRsp.setAccountNumber(originalRequest.getCreditorAccountNumber());
        if (rsp != null) {
            if (rsp.hasField(62)) {
                String privateData = rsp.getString(62);
                int cursor = 0;
                int endCursor = 20;
                debitTransferRsp.setNoRef(privateData.substring(cursor, endCursor));

                cursor = cursor + endCursor;
                endCursor = cursor + 4;
                String rc = privateData.substring(cursor, endCursor);

                cursor = cursor + endCursor;
                endCursor = cursor + 35;
                String rm = privateData.substring(cursor, endCursor);

                cursor = cursor + endCursor;
                endCursor = cursor + 34;
                debitTransferRsp.setAccountNumber(privateData.substring(cursor, endCursor));
                
                debitTransferRsp.setStatus(rc);
                debitTransferRsp.setReason(rm);
            } else {
                debitTransferRsp.setStatus(Constants.REASON_CODE_REJECT);
                debitTransferRsp.setReason("U904");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rsp);
        } else {
            debitTransferRsp.setStatus(Constants.REASON_CODE_KOMI_STATUS);
            debitTransferRsp.setReason("K000");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(rsp);
        }
    }

    @Override
    public ResponseEntity<DebitReversalInboundResponse> buildResponseMsg(BaseInboundRequestDTO request, ISOMsg isoMsg) {
        String privateData = isoMsg.getString(62);

        DebitReversalInboundResponse rsp = new DebitReversalInboundResponse();
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
