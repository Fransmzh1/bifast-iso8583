package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.CreditTransferOutboundRequest;
import com.mii.komi.dto.outbound.CreditTransferOutboundResponse;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.dto.outbound.requestroot.RootCreditTransfer;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.jpos.qbean.RestSender;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Utility;

import java.io.Serializable;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import org.jpos.util.NameRegistrar;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vinch
 */
public class CreditTransferOutboundParticipant extends OutboundParticipant {

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            RootCreditTransfer rootCreditTransfer = (RootCreditTransfer) buildRequestMsg(reqMsg);
            if(rootCreditTransfer.getCreditTransferRequest() == null) {
                ISOMsg rspMsg = (ISOMsg) reqMsg.clone();
                rspMsg.setResponseMTI();
                rspMsg.set(39, Constants.ISO_RSP_REJECTED);
                ctx.put(Constants.ISO_RESPONSE, rspMsg);
                return ABORTED | NO_JOIN;
            }
            ctx.put(Constants.HTTP_REQUEST, rootCreditTransfer);
            String endpointKomi = cfg.get("endpoint");
            RestSender restSender = NameRegistrar.get("komi-restsender");
            RestTemplate restTemplate = restSender.getRestTemplate();
            ParameterizedTypeReference<RestResponse<CreditTransferOutboundResponse>> typeRef
                    = new ParameterizedTypeReference<RestResponse<CreditTransferOutboundResponse>>() {
            };
            HttpEntity<RootCreditTransfer> entity = new HttpEntity<RootCreditTransfer>(rootCreditTransfer, restSender.getHeaders());
            ResponseEntity<RestResponse<CreditTransferOutboundResponse>> httpResponse
                    = restTemplate.exchange(endpointKomi, HttpMethod.POST, entity, typeRef);
            
            HttpStatus status = httpResponse.getStatusCode();
            ctx.put(Constants.HTTP_RESPONSE, httpResponse);
            
            // handle http 500
            if (status == HttpStatus.INTERNAL_SERVER_ERROR) return ABORTED;
            
            return PREPARED;
        } catch (DataNotFoundException ex) {
            ex.printStackTrace();
            return ABORTED;
        } catch (HttpRequestException | NameRegistrar.NotFoundException ex) {
            // use constants
            ctx.put(Constants.HTTP_RESPONSE,
                    ResponseEntity.internalServerError().body(RestResponse.failed(Constants.REASON_CODE_OTHER, ex.getMessage(), Constants.RESPONSE_CODE_REJECT)));
            return ABORTED;
        } catch (ISOException ex) {
            ex.printStackTrace();
            return ABORTED | NO_JOIN;
        }
    }

    @Override
    public Object buildRequestMsg(ISOMsg isoMsg) throws ISOException {
        String privateData = isoMsg.getString(48);
        String privateAdditionalData = isoMsg.getString(123);

        RootCreditTransfer root = new RootCreditTransfer();
        CreditTransferOutboundRequest req = new CreditTransferOutboundRequest();
        try {
            int cursor = 0;
            int endCursor = 20;
            req.setNoRef(privateData.substring(cursor, endCursor).trim());

            req.setTerminalId(isoMsg.getString(41).trim());

            cursor = endCursor;
            endCursor = cursor + 2;
            req.setCategoryPurpose(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 140;
            req.setDebtorName(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setDebtorType(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setDebtorId(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 34;
            req.setDebtorAccountNumber(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setDebtorAccountType(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setDebtorResidentialStatus(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setDebtorTownName(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 18;
            // Amount fix
            //req.setAmount(privateData.substring(cursor, endCursor).trim());
            req.setAmount(Utility.getJSONMoney(privateData.substring(cursor, endCursor)));

            cursor = endCursor;
            endCursor = cursor + 18;
            // Amount fix
            //req.setFeeTransfer(privateData.substring(cursor, endCursor).trim());
            req.setFeeTransfer(Utility.getJSONMoney(privateData.substring(cursor, endCursor)));

            cursor = 0;
            endCursor = cursor + 35;
            req.setRecipientBank(privateAdditionalData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 140;
            req.setCreditorName(privateAdditionalData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setCreditorType(privateAdditionalData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setCreditorId(privateAdditionalData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 34;
            req.setCreditorAccountNumber(privateAdditionalData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setCreditorAccountType(privateAdditionalData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setCreditorResidentialStatus(privateAdditionalData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setCreditorTownName(privateAdditionalData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 140;
            req.setCreditorProxyId(privateAdditionalData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setCreditorProxyType(privateAdditionalData.substring(cursor, endCursor).trim());

            // force length
            cursor = endCursor;
            endCursor = cursor + 140;
            req.setPaymentInformation(privateAdditionalData.substring(cursor, endCursor).trim());

            root.setCreditTransferRequest(req);
        } catch (StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            root.setCreditTransferRequest(null);
        }
        return root;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr) {
        ISOMsg isoRsp = super.buildFailedResponseMsg(req, rr);

        String responseCode = null;
        String reasonCode = null;
        String reasonMessage = "";
        CreditTransferOutboundResponse creditTransferResponse = null;
        if (rr.hasBody() && rr.getBody().getContent() != null && rr.getBody().getContent().size() > 0) {
            creditTransferResponse = (CreditTransferOutboundResponse) rr.getBody().getContent().get(0);
            responseCode = rr.getBody().getResponseCode();
            reasonCode = rr.getBody().getReasonCode();
            reasonMessage = rr.getBody().getReasonMessage();
        }
        else {
            creditTransferResponse = new CreditTransferOutboundResponse();
            creditTransferResponse.setNoRef(req.getString(63));
            try {
                responseCode = rr.getBody().getResponseCode();
                reasonCode = rr.getBody().getReasonCode();
                reasonMessage = rr.getBody().getReasonMessage();
            }
            catch (Exception e) {
                // nothing
            }
            if (responseCode == null) responseCode = Constants.RESPONSE_CODE_REJECT;
            if (reasonCode == null) reasonCode = Constants.REASON_CODE_OTHER;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(creditTransferResponse.getNoRef(), 20))
                .append(ISOUtil.strpad(responseCode, 4))
                .append(ISOUtil.strpad(reasonCode, 35))
                .append(ISOUtil.strpad("", 35))
                .append(ISOUtil.strpad(reasonMessage, 140))
                .append(ISOUtil.strpad(creditTransferResponse.getAccountNumber(), 34))
                .append(ISOUtil.strpad(creditTransferResponse.getCreditorName(), 35));
        isoRsp.set(62, sb.toString());
        return isoRsp;
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> dto) {
        ISOMsg isoRsp = super.buildResponseMsg(req, dto);
        CreditTransferOutboundResponse creditTransferResponse = (CreditTransferOutboundResponse) dto.getBody().getContent().get(0);
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(creditTransferResponse.getNoRef(), 20))
                .append(ISOUtil.strpad(dto.getBody().getResponseCode(), 4))
                .append(ISOUtil.strpad(dto.getBody().getReasonCode(), 35))
                .append(ISOUtil.strpad("", 35))
                .append(ISOUtil.strpad(dto.getBody().getReasonMessage(), 140))
                .append(ISOUtil.strpad(creditTransferResponse.getAccountNumber(), 34))
                .append(ISOUtil.strpad(creditTransferResponse.getCreditorName(), 35));
        isoRsp.set(62, sb.toString());
        return isoRsp;
    }

}
