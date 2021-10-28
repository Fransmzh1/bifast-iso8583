package com.mii.komi.jpos.participant.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.ProxyResolutionRequest;
import com.mii.komi.dto.outbound.ProxyResolutionResponse;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.dto.outbound.requestroot.RootProxyResolution;
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

public class ProxyResolutionParticipant extends OutboundParticipant {


    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            RootProxyResolution rootProxyResolution = (RootProxyResolution) buildRequestMsg(reqMsg);
            ctx.put(Constants.HTTP_REQUEST, rootProxyResolution);
            String endpointKomi = cfg.get("endpoint");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
            ParameterizedTypeReference<RestResponse<ProxyResolutionResponse>> typeRef
                    = new ParameterizedTypeReference<RestResponse<ProxyResolutionResponse>>() {
            };
            HttpEntity<RootProxyResolution> entity = new HttpEntity<RootProxyResolution>(rootProxyResolution);
            ResponseEntity<RestResponse<ProxyResolutionResponse>> proxyResolutionResponse
                    = restTemplate.exchange(endpointKomi, HttpMethod.POST, entity, typeRef);
            ctx.put(Constants.HTTP_RESPONSE, proxyResolutionResponse);
            return PREPARED;
        } catch (DataNotFoundException ex) {
            ex.printStackTrace();
            return ABORTED;
        } catch (HttpRequestException ex) {
            if (ex.getMessage() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                ProxyResolutionResponse proxyResolutionResponse;
                try {
                    proxyResolutionResponse = objectMapper.readValue(ex.getMessage(), ProxyResolutionResponse.class);
                    ctx.put(Constants.HTTP_RESPONSE, proxyResolutionResponse);
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

        RootProxyResolution root = new RootProxyResolution();
        ProxyResolutionRequest req = new ProxyResolutionRequest();
        int cursor = 0;
        int endCursor = 20;
        req.setNoRef(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 4;
        // not used
        //req.setLookupType(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 34;
        req.setSenderAccountNumber(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 12;
        req.setProxyType(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        req.setProxyValue(privateData.substring(cursor));

        root.setProxyResolutionRequest(req);

        return root;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr) {
        try {
            //String privateData = req.getString(48);

            ProxyResolutionResponse proxyResolutionResponse = (ProxyResolutionResponse) rr.getBody().getContent().get(0);
            ISOMsg isoRsp = (ISOMsg) req.clone();
            isoRsp.setResponseMTI();

            isoRsp.set(39, "81"); // 80?
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(proxyResolutionResponse.getNoRef(), 20))
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
        ProxyResolutionResponse proxyResolutionResponse = (ProxyResolutionResponse) dto.getBody().getContent().get(0);
        ISOMsg isoRsp = (ISOMsg) req.clone();
        isoRsp.setResponseMTI();
        isoRsp.set(39, "00"); // check responseCode = "ACTC", "RJCT", "OTHR", "KSTS" ?
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(proxyResolutionResponse.getNoRef(), 20))
                .append(ISOUtil.strpad(dto.getBody().getResponseCode(), 4))
                .append(ISOUtil.strpad(dto.getBody().getReasonCode(), 35))
                .append(ISOUtil.strpad(proxyResolutionResponse.getProxyType(), 12))
                .append(ISOUtil.strpad(proxyResolutionResponse.getProxyValue(), 140))
                .append(ISOUtil.strpad(proxyResolutionResponse.getRegistrationId(), 35))
                .append(ISOUtil.strpad(proxyResolutionResponse.getDisplayName(), 140))
                .append(ISOUtil.strpad(proxyResolutionResponse.getRegisterBank(), 35))
                .append(ISOUtil.strpad(proxyResolutionResponse.getAccountNumber(), 34))
                .append(ISOUtil.strpad(proxyResolutionResponse.getAccountType(), 4))
                // below are optionals
                .append(ISOUtil.strpad(proxyResolutionResponse.getAccountName(), 140))
                .append(ISOUtil.strpad(proxyResolutionResponse.getCustomerType(), 35))
                .append(ISOUtil.strpad(proxyResolutionResponse.getCustomerId(), 35))
                .append(ISOUtil.strpad(proxyResolutionResponse.getResidentialStatus(), 35))
                .append(ISOUtil.strpad(proxyResolutionResponse.getTownName(), 35));
        isoRsp.set(62, sb.toString());
        return isoRsp;
    }
    
}
