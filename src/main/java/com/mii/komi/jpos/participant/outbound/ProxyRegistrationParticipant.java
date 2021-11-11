package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.ProxyRegistrationRequest;
import com.mii.komi.dto.outbound.ProxyRegistrationResponse;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.dto.outbound.requestroot.RootProxyRegistration;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.jpos.qbean.RestSender;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import static org.jpos.transaction.TransactionConstants.ABORTED;
import static org.jpos.transaction.TransactionConstants.PREPARED;
import org.jpos.util.NameRegistrar;
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
            if(rootProxyRegistration.getProxyRegistrationRequest() == null) {
                ISOMsg rspMsg = (ISOMsg) reqMsg.clone();
                rspMsg.setResponseMTI();
                rspMsg.set(39, Constants.ISO_RSP_REJECTED);
                //rspMsg.unset(48);
                ctx.put(Constants.ISO_RESPONSE, rspMsg);
                return ABORTED | NO_JOIN;
            }
            ctx.put(Constants.HTTP_REQUEST, rootProxyRegistration);
            String endpointKomi = cfg.get("endpoint");
            RestSender restSender = NameRegistrar.get("komi-restsender");
            RestTemplate restTemplate = restSender.getRestTemplate();
            ParameterizedTypeReference<RestResponse<ProxyRegistrationResponse>> typeRef
                    = new ParameterizedTypeReference<RestResponse<ProxyRegistrationResponse>>() {
            };
            HttpEntity<RootProxyRegistration> entity = new HttpEntity<RootProxyRegistration>(rootProxyRegistration, restSender.getHeaders());
            ResponseEntity<RestResponse<ProxyRegistrationResponse>> httpResponse
                    = restTemplate.exchange(endpointKomi, HttpMethod.POST, entity, typeRef);
            ctx.put(Constants.HTTP_RESPONSE, httpResponse);
            return PREPARED;
        } catch (DataNotFoundException ex) {
            ex.printStackTrace();
            return ABORTED;
        } catch (HttpRequestException | NameRegistrar.NotFoundException ex) {
            ctx.put(Constants.HTTP_RESPONSE,
                    ResponseEntity.internalServerError().body(RestResponse.failed("K000", ex.getMessage(), "RJCT")));
            return ABORTED;
        } catch (ISOException ex) {
            ex.printStackTrace();
            return ABORTED | NO_JOIN;
        }
    }

    @Override
    public Object buildRequestMsg(ISOMsg isoMsg) throws ISOException {
        String privateData = isoMsg.getString(48);

        RootProxyRegistration root = new RootProxyRegistration();
        ProxyRegistrationRequest req = new ProxyRegistrationRequest();

        try {

            int cursor = 0;
            int endCursor = 20;
            req.setNoRef(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 4;
            req.setRegistrationType(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 12;
            req.setProxyType(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 140;
            req.setProxyValue(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 140;
            req.setDisplayName(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 34;
            req.setAccountNumber(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 4;
            req.setAccountType(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 140;
            req.setAccountName(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 12;
            req.setSecondaryIdType(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setSecondaryIdValue(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setCustomerType(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setCustomerId(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 35;
            req.setResidentialStatus(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            req.setTownName(privateData.substring(cursor).trim());

            root.setProxyRegistrationRequest(req);
        } catch (StringIndexOutOfBoundsException ex) {
            root.setProxyRegistrationRequest(null);
        }

        return root;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr) {
        ISOMsg isoRsp = super.buildFailedResponseMsg(req, rr);
        if (rr.hasBody() && rr.getBody().getContent() != null && rr.getBody().getContent().size() > 0) {
            ProxyRegistrationResponse proxyRegistrationResponse = (ProxyRegistrationResponse) rr.getBody().getContent().get(0);
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(proxyRegistrationResponse.getNoRef(), 20))
                    .append(ISOUtil.strpad(rr.getBody().getResponseCode(), 4))
                    .append(ISOUtil.strpad(rr.getBody().getReasonCode(), 35))
                    .append(proxyRegistrationResponse.getRegistrationType())
                    .append(proxyRegistrationResponse.getRegistrationId());
            isoRsp.set(62, sb.toString());
        }
        return isoRsp;
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> dto) {
        ISOMsg isoRsp = super.buildResponseMsg(req, dto);

        ProxyRegistrationResponse proxyRegistrationResponse = (ProxyRegistrationResponse) dto.getBody().getContent().get(0);
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
