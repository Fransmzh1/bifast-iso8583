package com.mii.komi.jpos.participant.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            if (ex.getMessage() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                CreditTransferOutboundResponse creditTransferRsp;
                try {
                    creditTransferRsp = objectMapper.readValue(ex.getMessage(), CreditTransferOutboundResponse.class);
                    ctx.put(Constants.HTTP_RESPONSE, creditTransferRsp);
                } catch (JsonProcessingException ex1) {
                    ex1.printStackTrace();
                }
            }
            return ABORTED;
        } catch (ISOException ex) {
            ex.printStackTrace();
            return ABORTED;
        }
    }

    @Override
    public Object buildRequestMsg(ISOMsg isoMsg) throws ISOException {
        String privateData = isoMsg.getString(48);

        RootCreditTransfer root = new RootCreditTransfer();
        CreditTransferOutboundRequest req = new CreditTransferOutboundRequest();
        int cursor = 0;
        int endCursor = 20;
        req.setNoRef(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 20;
        req.setTerminalId(isoMsg.getString(41).trim());
        
        cursor = endCursor;
        endCursor = cursor + 2;
        req.setCategoryPurpose(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 140;
        req.setDebtorName(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setDebtorType(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setDebtorId(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 34;
        req.setDebtorAccountNumber(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setDebtorType(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setDebtorResidentialStatus(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setDebtorTownName(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 18;
        req.setAmount(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 18;
        req.setFeeTransfer(privateData.substring(cursor, endCursor));

        root.setCreditTransferRequest(req);

        return root;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr) {
        try {
            CreditTransferOutboundResponse creditTransferResponse = (CreditTransferOutboundResponse) rr.getBody().getContent().get(0);
            ISOMsg isoRsp = (ISOMsg) req.clone();
            isoRsp.setResponseMTI();
            isoRsp.set(39, "81");
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(creditTransferResponse.getNoRef(), 20))
                .append(ISOUtil.strpad(rr.getBody().getResponseCode(), 4))
                .append(ISOUtil.strpad(rr.getBody().getReasonCode(), 35))
                .append(ISOUtil.strpad("", 35))
                .append(ISOUtil.strpad(rr.getBody().getResponseMessage(), 140))
                .append(ISOUtil.strpad("", 34))
                .append(ISOUtil.strpad("", 35));
            isoRsp.set(62, sb.toString());
            return isoRsp;
        } catch (ISOException ex) {
            return null;
        }
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> dto) throws ISOException {
        CreditTransferOutboundResponse creditTransferResponse = (CreditTransferOutboundResponse) dto.getBody().getContent().get(0);
        String privateData = req.getString(48);
        ISOMsg isoRsp = (ISOMsg) req.clone();
        isoRsp.setResponseMTI();
        isoRsp.set(39, "00");
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(creditTransferResponse.getNoRef(), 20))
                .append(ISOUtil.strpad(dto.getBody().getResponseCode(), 4))
                .append(ISOUtil.strpad(dto.getBody().getReasonCode(), 35))
                .append(ISOUtil.strpad("", 35))
                .append(ISOUtil.strpad(dto.getBody().getResponseMessage(), 140))
                .append(ISOUtil.strpad(creditTransferResponse.getAccountNumber(), 34))
                .append(ISOUtil.strpad(creditTransferResponse.getCreditorName(), 35));
        isoRsp.set(62, sb.toString());
        return isoRsp;
    }
    
}
