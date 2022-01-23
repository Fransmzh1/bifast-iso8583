package com.mii.komi.jpos.participant.outbound;

import java.io.Serializable;

import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.ProxyResolutionRequest;
import com.mii.komi.dto.outbound.ProxyResolutionResponse;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.dto.outbound.requestroot.RootProxyResolution;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.jpos.qbean.RestSender;
import com.mii.komi.util.Constants;

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

public class ProxyResolutionParticipant extends OutboundParticipant {

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            RootProxyResolution rootProxyResolution = (RootProxyResolution) buildRequestMsg(reqMsg);
            if(rootProxyResolution.getProxyResolutionRequest() == null) {
                ISOMsg rspMsg = (ISOMsg) reqMsg.clone();
                rspMsg.setResponseMTI();
                rspMsg.set(39, Constants.ISO_RSP_REJECTED);
                ctx.put(Constants.ISO_RESPONSE, rspMsg);
                return ABORTED | NO_JOIN;
            }
            ctx.put(Constants.HTTP_REQUEST, rootProxyResolution);
            String endpointKomi = cfg.get("endpoint");
            RestSender restSender = NameRegistrar.get("komi-restsender");
            RestTemplate restTemplate = restSender.getRestTemplate();
            ParameterizedTypeReference<RestResponse<ProxyResolutionResponse>> typeRef
                    = new ParameterizedTypeReference<RestResponse<ProxyResolutionResponse>>() {
            };
            HttpEntity<RootProxyResolution> entity = new HttpEntity<RootProxyResolution>(rootProxyResolution, restSender.getHeaders());
            ResponseEntity<RestResponse<ProxyResolutionResponse>> proxyResolutionResponse
                    = restTemplate.exchange(endpointKomi, HttpMethod.POST, entity, typeRef);
            ctx.put(Constants.HTTP_RESPONSE, proxyResolutionResponse);

            // handle http 500
            if (proxyResolutionResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) return ABORTED;

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
            return ABORTED;
        }
    }

    @Override
    public Object buildRequestMsg(ISOMsg isoMsg) throws ISOException {
        String privateData = isoMsg.getString(48);

        RootProxyResolution root = new RootProxyResolution();
        ProxyResolutionRequest req = new ProxyResolutionRequest();

        try {
            int cursor = 0;
            int endCursor = 20;
            req.setNoRef(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 4;
            req.setLookupType(privateData.substring(cursor, endCursor));

            cursor = endCursor;
            endCursor = cursor + 34;
            req.setSenderAccountNumber(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 12;
            req.setProxyType(privateData.substring(cursor, endCursor).trim());

            // force length
            cursor = endCursor;
            endCursor = cursor + 140;
            req.setProxyValue(privateData.substring(cursor, endCursor).trim());

            root.setProxyResolutionRequest(req);
        } catch (StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            root.setProxyResolutionRequest(null);
        }

        return root;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr) {
        ISOMsg isoRsp = super.buildFailedResponseMsg(req, rr);

        // handles : body with no-content, body n/a
        String responseCode = null;
        String reasonCode = null;
        ProxyResolutionResponse proxyResolutionResponse = null;
        if (rr.hasBody() && rr.getBody().getContent() != null && rr.getBody().getContent().size() > 0) {
            proxyResolutionResponse = (ProxyResolutionResponse) rr.getBody().getContent().get(0);
            responseCode = rr.getBody().getResponseCode();
            reasonCode = rr.getBody().getReasonCode();
        }
        else {
            proxyResolutionResponse = new ProxyResolutionResponse(); // empty response
            proxyResolutionResponse.setNoRef(req.getString(63)); // set noRef from request
            try {
                responseCode = rr.getBody().getResponseCode();
                reasonCode = rr.getBody().getReasonCode();
            }
            catch (Exception e) {
                // nothing
            }
            if (responseCode == null) responseCode = Constants.RESPONSE_CODE_REJECT;
            if (reasonCode == null) reasonCode = Constants.REASON_CODE_OTHER;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(proxyResolutionResponse.getNoRef(), 20))
                .append(ISOUtil.strpad(responseCode, 4))
                .append(ISOUtil.strpad(reasonCode, 35))
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

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> dto) {
        ISOMsg isoRsp = super.buildResponseMsg(req, dto);

        ProxyResolutionResponse proxyResolutionResponse = (ProxyResolutionResponse) dto.getBody().getContent().get(0);
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
