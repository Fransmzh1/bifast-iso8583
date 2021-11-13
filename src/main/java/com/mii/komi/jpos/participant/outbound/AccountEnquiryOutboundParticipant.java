package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.outbound.AccountEnquiryOutboundRequest;
import com.mii.komi.dto.outbound.AccountEnquiryOutboundResponse;
import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.dto.outbound.requestroot.RootAccountEnquiry;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.jpos.qbean.RestSender;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Utility;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import org.jpos.util.NameRegistrar;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class AccountEnquiryOutboundParticipant extends OutboundParticipant {

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            RootAccountEnquiry accountEnquiryRequest = buildRequestMsg(reqMsg);
            if(accountEnquiryRequest.getAccountEnquiryRequest() == null) {
                ISOMsg rspMsg = (ISOMsg) reqMsg.clone();
                rspMsg.setResponseMTI();
                rspMsg.set(39, Constants.ISO_RSP_REJECTED);
                rspMsg.unset(48);
                ctx.put(Constants.ISO_RESPONSE, rspMsg);
                return ABORTED | NO_JOIN;
            }
            ctx.put(Constants.HTTP_REQUEST, accountEnquiryRequest);
            String endpointKomi = cfg.get("endpoint");
            RestSender restSender = NameRegistrar.get("komi-restsender");
            RestTemplate restTemplate = restSender.getRestTemplate();
            ParameterizedTypeReference<RestResponse<AccountEnquiryOutboundResponse>> typeRef
                    = new ParameterizedTypeReference<RestResponse<AccountEnquiryOutboundResponse>>() {
            };
            HttpEntity<RootAccountEnquiry> entity = new HttpEntity<RootAccountEnquiry>(accountEnquiryRequest, restSender.getHeaders());
            ResponseEntity<RestResponse<AccountEnquiryOutboundResponse>> accountEnquiryResponse
                    = restTemplate.exchange(endpointKomi, HttpMethod.POST, entity, typeRef);
            ctx.put(Constants.HTTP_RESPONSE, accountEnquiryResponse);
            return PREPARED;
        } catch (DataNotFoundException ex) {
            ctx.put(Constants.HTTP_RESPONSE,
                    ResponseEntity.internalServerError().body(RestResponse.failed("K000", ex.getMessage(), "RJCT")));
            return ABORTED;
        } catch (HttpRequestException ex) {
            ctx.put(Constants.HTTP_RESPONSE,
                    ResponseEntity.internalServerError().body(RestResponse.failed("K000", ex.getMessage(), "RJCT")));
            return ABORTED;
        } catch (ISOException ex) {
            Logger.getLogger(AccountEnquiryOutboundParticipant.class.getName()).log(Level.SEVERE, null, ex);
            return ABORTED | NO_JOIN;
        } catch (NameRegistrar.NotFoundException ex) {
            Logger.getLogger(AccountEnquiryOutboundParticipant.class.getName()).log(Level.SEVERE, null, ex);
            return ABORTED | NO_JOIN;
        }
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> response) {
        try {
            ISOMsg isoRsp = super.buildResponseMsg(req, response);
            AccountEnquiryOutboundResponse accountEnquiryRsp = (AccountEnquiryOutboundResponse) response.getBody().getContent().get(0);
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(accountEnquiryRsp.getNoRef(), 20))
                    .append(ISOUtil.strpad(response.getBody().getResponseCode(), 4))
                    .append(ISOUtil.strpad(response.getBody().getReasonCode(), 35))
                    .append(ISOUtil.zeropad(accountEnquiryRsp.getAccountNumber(), 34))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getAccountType(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorName(), 140))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorId(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorType(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getResidentStatus(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getTownName(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getProxyId(), 140))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getProxyType(), 35));
            isoRsp.set(62, sb.toString());
            return isoRsp;
        } catch (ISOException ex) {
            Logger.getLogger(AccountEnquiryOutboundParticipant.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public RootAccountEnquiry buildRequestMsg(ISOMsg isoMsg) {
        String privateData = isoMsg.getString(48);
        RootAccountEnquiry root = new RootAccountEnquiry();
        AccountEnquiryOutboundRequest req = new AccountEnquiryOutboundRequest();
        try {
            int cursor = 0;
            int endCursor = 20;
            req.setNoRef(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 34;
            req.setSenderAccountNumber(privateData.substring(cursor, endCursor).trim());

            cursor = endCursor;
            endCursor = cursor + 18;
            // Amount fix
            //req.setAmount(privateData.substring(cursor, endCursor).trim());
            req.setAmount(Utility.getJSONMoney(privateData.substring(cursor, endCursor)));

            cursor = endCursor;
            endCursor = cursor + 2;
            req.setCategoryPurpose(privateData.substring(cursor, endCursor).trim());

            if (privateData.length() > endCursor) {
                cursor = endCursor;
                endCursor = cursor + 34;
                req.setRecipientBank(privateData.substring(cursor, endCursor).trim());

                cursor = endCursor;
                endCursor = cursor + 34;
                req.setRecipientAccountNumber(privateData.substring(cursor, endCursor).trim());

                if (privateData.length() > endCursor) {
                    cursor = endCursor;
                    endCursor = cursor + 140;
                    req.setProxyId(privateData.substring(cursor, endCursor).trim());

                    cursor = endCursor;
                    // update : adam, 140 -> 35
                    endCursor = cursor + 35;
                    req.setProxyType(privateData.substring(cursor, endCursor).trim());
                }
            }
            root.setAccountEnquiryRequest(req);
        } catch (StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            root.setAccountEnquiryRequest(null);
        }
        return root;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr) {
        ISOMsg isoRsp = super.buildFailedResponseMsg(req, rr);
        if (rr.hasBody() && rr.getBody().getContent() != null && rr.getBody().getContent().size() > 0) {
            AccountEnquiryOutboundResponse accountEnquiryRsp = (AccountEnquiryOutboundResponse) rr.getBody().getContent().get(0);
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(accountEnquiryRsp.getNoRef(), 20))
                    .append(ISOUtil.strpad(rr.getBody().getResponseCode(), 4))
                    .append(ISOUtil.strpad(rr.getBody().getReasonCode(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getAccountNumber(), 34))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getAccountType(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorName(), 140))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorId(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorType(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getResidentStatus(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getTownName(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getProxyId(), 140))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getProxyType(), 35));
            isoRsp.set(62, sb.toString());
        }
        return isoRsp;
    }

}
