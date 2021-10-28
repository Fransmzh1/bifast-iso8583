package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.CreditTransferOutboundRequest;
import com.mii.komi.dto.outbound.CreditTransferOutboundResponse;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.dto.outbound.requestroot.RootCreditTransfer;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.exception.RestTemplateResponseErrorHandler;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
                rspMsg.set(62, reqMsg.getString(63) + 
                        Constants.RESPONSE_CODE_REJECT + 
                        ISOUtil.strpad(Constants.REASON_CODE_UNDEFINED, 35));
                ctx.put(Constants.ISO_RESPONSE, rspMsg);
                return ABORTED | NO_JOIN;
            }
            ctx.put(Constants.HTTP_REQUEST, rootCreditTransfer);
            String endpointKomi = cfg.get("endpoint");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
            ParameterizedTypeReference<RestResponse<CreditTransferOutboundResponse>> typeRef
                    = new ParameterizedTypeReference<RestResponse<CreditTransferOutboundResponse>>() {
            };
            HttpEntity<RootCreditTransfer> entity = new HttpEntity<RootCreditTransfer>(rootCreditTransfer);
            ResponseEntity<RestResponse<CreditTransferOutboundResponse>> httpResponse
                    = restTemplate.exchange(endpointKomi, HttpMethod.POST, entity, typeRef);
            ctx.put(Constants.HTTP_RESPONSE, httpResponse);
            return PREPARED;
        } catch (DataNotFoundException ex) {
            ex.printStackTrace();
            return ABORTED;
        } catch (HttpRequestException ex) {
            ctx.put(Constants.HTTP_RESPONSE,
                    ResponseEntity.internalServerError().body(RestResponse.failed("K000", ex.getMessage(), "KSTS")));
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
            req.setAmount(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 18;
            req.setFeeTransfer(privateData.substring(cursor, endCursor).trim());

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

            cursor = endCursor;
            req.setPaymentInformation(privateAdditionalData.substring(cursor).trim());

            root.setCreditTransferRequest(req);
        } catch (ArrayIndexOutOfBoundsException ex) {
            root.setCreditTransferRequest(null);
        }
        return root;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr) {
        ISOMsg isoRsp = super.buildFailedResponseMsg(req, rr);
        isoRsp.set(39, Constants.ISO_RSP_UNDEFINED);
        if (rr.hasBody()) {
            CreditTransferOutboundResponse creditTransferResponse = (CreditTransferOutboundResponse) rr.getBody().getContent().get(0);
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(creditTransferResponse.getNoRef(), 20))
                    .append(ISOUtil.strpad(rr.getBody().getResponseCode(), 4))
                    .append(ISOUtil.strpad(rr.getBody().getReasonCode(), 35))
                    .append(ISOUtil.strpad("", 35))
                    .append(ISOUtil.strpad(rr.getBody().getReasonMessage(), 140))
                    .append(ISOUtil.strpad("", 34))
                    .append(ISOUtil.strpad("", 35));
            isoRsp.set(62, sb.toString());
        }
        return isoRsp;
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> dto) {
        ISOMsg isoRsp = super.buildResponseMsg(req, dto);
        if (Constants.RESPONSE_CODE_KOMI_STATUS.equals(dto.getBody().getResponseCode())
                && Constants.REASON_CODE_UNDEFINED.equals(dto.getBody().getReasonCode())) {
            isoRsp.set(39, Constants.ISO_RSP_UNDEFINED);
        }

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
