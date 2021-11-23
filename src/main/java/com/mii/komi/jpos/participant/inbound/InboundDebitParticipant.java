package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.DebitTransferInboundRequest;
import com.mii.komi.dto.inbound.DebitTransferInboundResponse;
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
public class InboundDebitParticipant extends GenericInboundParticipantImpl {

    public static String DEBIT_TRANSFER_PC = "109000";

    Log log = Log.getLog("Q2", this.getClass().getName());

    @Override
    public ISOMsg buildRequestMsg(long id, Serializable context) {
        try {
            Context ctx = (Context) context;
            DebitTransferInboundRequest debitRequest = (DebitTransferInboundRequest) ctx.get(Constants.HTTP_REQUEST);

            StringBuilder content48 = new StringBuilder();
            String noRef = ISOUtil.strpad(debitRequest.getNoRef(), 20);
            String originalNoRef = ISOUtil.strpad(debitRequest.getOriginalNoRef(), 20);
            String originalDateTime = ISOUtil.strpad(Utility.getISODateTime(debitRequest.getOriginalDateTime()), 10);
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
                    .append(debtorAccountType).append(debtorResidentStatus)
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

            ISOMsg isoMsg = ISO8583Service.buildFinancialMsg(DEBIT_TRANSFER_PC, debitRequest);
            isoMsg.set(48, content48.toString());
            isoMsg.set(123, content123.toString());

            return isoMsg;
        } catch (ISOException ex) {
            log.error(Utility.getExceptionStackTraceAsString(ex));
            return null;
        }
    }

    @Override
    public ResponseEntity buildSpesificRspBody(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg isoMsg = ctx.get(Constants.ISO_RESPONSE);

        DebitTransferInboundRequest request = (DebitTransferInboundRequest) ctx.get(Constants.HTTP_REQUEST);
        DebitTransferInboundResponse rsp = new DebitTransferInboundResponse();
        rsp.setTransactionId(request.getTransactionId());
        rsp.setDateTime(request.getDateTime());
        rsp.setMerchantType(isoMsg.getString(18));
        rsp.setTerminalId(isoMsg.getString(41));

        String privateData = isoMsg.getString(62);

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
