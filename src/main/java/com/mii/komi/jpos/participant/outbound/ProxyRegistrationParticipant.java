package com.mii.komi.jpos.participant.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.ProxyRegistrationRequest;
import com.mii.komi.dto.outbound.ProxyRegistrationResponse;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.dto.outbound.requestroot.RootProxyRegistration;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.exception.RestTemplateResponseErrorHandler;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import static org.jpos.transaction.TransactionConstants.ABORTED;
import static org.jpos.transaction.TransactionConstants.PREPARED;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vinch
 */
public class ProxyRegistrationParticipant extends OutboundParticipant {

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            RootProxyRegistration rootProxyRegistration = (RootProxyRegistration) buildRequestMsg(reqMsg);
            ctx.put(Constants.HTTP_REQUEST, rootProxyRegistration);
            String endpointKomi = cfg.get("endpoint");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
            ParameterizedTypeReference<RestResponse<ProxyRegistrationResponse>> typeRef
                    = new ParameterizedTypeReference<RestResponse<ProxyRegistrationResponse>>() {
            };
            HttpEntity<RootProxyRegistration> entity = new HttpEntity<RootProxyRegistration>(rootProxyRegistration);
            ResponseEntity<RestResponse<ProxyRegistrationResponse>> httpResponse
                    = restTemplate.exchange(endpointKomi, HttpMethod.POST, entity, typeRef);
            ctx.put(Constants.HTTP_RESPONSE, httpResponse);
            return PREPARED;
        } catch (DataNotFoundException ex) {
            ex.printStackTrace();
            return ABORTED;
        } catch (HttpRequestException ex) {
            if (ex.getMessage() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                ProxyRegistrationResponse proxyRegistrationRsp;
                try {
                    proxyRegistrationRsp = objectMapper.readValue(ex.getMessage(), ProxyRegistrationResponse.class);
                    ctx.put(Constants.HTTP_RESPONSE, proxyRegistrationRsp);
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

        RootProxyRegistration root = new RootProxyRegistration();
        ProxyRegistrationRequest req = new ProxyRegistrationRequest();
        int cursor = 0;
        int endCursor = 20;
        req.setNoRef(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 4;
        req.setRegistrationType(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 12;
        req.setProxyType(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 140;
        req.setProxyValue(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 140;
        req.setDisplayName(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 34;
        req.setAccountNumber(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 4;
        req.setAccountType(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 140;
        req.setAccountName(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 12;
        req.setSecondaryIdType(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setSecondaryIdValue(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setCustomerType(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setCustomerId(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setResidentialStatus(privateData.substring(cursor, endCursor));
        
        cursor = endCursor;
        endCursor = cursor + 35;
        req.setTownName(privateData.substring(cursor, endCursor));

        root.setProxyRegistrationRequest(req);

        return root;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr) {
        try {
            ProxyRegistrationResponse proxyRegistrationResponse = (ProxyRegistrationResponse) rr.getBody().getContent().get(0);
            ISOMsg isoRsp = (ISOMsg) req.clone();
            isoRsp.setResponseMTI();
            isoRsp.set(39, "81");
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(proxyRegistrationResponse.getNoRef(), 20))
                    .append(ISOUtil.strpad(rr.getBody().getResponseCode(), 4))
                    .append(ISOUtil.strpad(rr.getBody().getReasonCode(), 35));
            isoRsp.set(62, sb.toString());
            return isoRsp;
        } catch (ISOException ex) {
            return null;
        }
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> dto) throws ISOException {
        ProxyRegistrationResponse proxyRegistrationResponse = (ProxyRegistrationResponse) dto.getBody().getContent().get(0);
        String privateData = req.getString(48);
        ISOMsg isoRsp = (ISOMsg) req.clone();
        isoRsp.setResponseMTI();
        isoRsp.set(39, "00");
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(proxyRegistrationResponse.getNoRef(), 20))
                .append(ISOUtil.strpad(dto.getBody().getResponseCode(), 4))
                .append(ISOUtil.strpad(dto.getBody().getReasonCode(), 35))
                .append(proxyRegistrationResponse.getRegistrationType())
                .append(proxyRegistrationResponse.getRegistrationId());
        isoRsp.set(62, sb.toString());
        return isoRsp;
    }
    
}
