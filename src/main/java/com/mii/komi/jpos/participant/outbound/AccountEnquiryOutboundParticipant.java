package com.mii.komi.jpos.participant.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mii.komi.dto.outbound.AccountEnquiryOutboundRequest;
import com.mii.komi.dto.outbound.AccountEnquiryOutboundResponse;
import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.exception.RestTemplateResponseErrorHandler;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.core.Configuration;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class AccountEnquiryOutboundParticipant extends OutboundParticipant {

    private Configuration cfg;

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            AccountEnquiryOutboundRequest accountEnquiryRequest = buildRequestMsg(reqMsg);
            ctx.put(Constants.HTTP_REQUEST, accountEnquiryRequest);
            String endpointKomi = cfg.get("endpoint");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
            RestResponse<AccountEnquiryOutboundResponse> accountEnquiryResponse = restTemplate.postForObject(endpointKomi,
                    accountEnquiryRequest,
                    RestResponse.class);
            ctx.put(Constants.HTTP_RESPONSE, accountEnquiryResponse);
            return PREPARED;
        } catch (DataNotFoundException ex) {
            ex.printStackTrace();
            return ABORTED;
        } catch (HttpRequestException ex) {
            if (ex.getMessage() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                AccountEnquiryOutboundResponse accountEnquiryResponse;
                try {
                    accountEnquiryResponse = objectMapper.readValue(ex.getMessage(), AccountEnquiryOutboundResponse.class);
                    ctx.put(Constants.HTTP_RESPONSE, accountEnquiryResponse);
                } catch (JsonProcessingException ex1) {
                    ex1.printStackTrace();
                }
            }
            return ABORTED;
        }
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, RestResponse<BaseOutboundDTO> response) throws ISOException {
        AccountEnquiryOutboundResponse accountEnquiryRsp = (AccountEnquiryOutboundResponse) response.getContent().get(0);
        ISOMsg isoRsp = (ISOMsg) req.clone();
        isoRsp.setResponseMTI();
        isoRsp.set(39, "00");
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(accountEnquiryRsp.getNoRef(), 20))
                .append(ISOUtil.strpad(response.getResponseCode(), 4))
                .append(ISOUtil.strpad(response.getReasonCode(), 35))
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
    }

    @Override
    public AccountEnquiryOutboundRequest buildRequestMsg(ISOMsg isoMsg) {
        String privateData = isoMsg.getString(48);

        AccountEnquiryOutboundRequest req = new AccountEnquiryOutboundRequest();
        int cursor = 0;
        int endCursor = 20;
        req.setNoRef(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 34;
        req.setSenderAccountNumber(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 18;
        req.setAmount(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 2;
        req.setCategoryPurpose(privateData.substring(cursor, endCursor));

        if (privateData.length() > endCursor) {
            cursor = endCursor;
            endCursor = cursor + 34;
            req.setRecipientBank(privateData.substring(cursor, endCursor));

            cursor = endCursor;
            endCursor = cursor + 34;
            req.setRecipientAccountNumber(privateData.substring(cursor, endCursor));

            if (privateData.length() > endCursor) {
                cursor = endCursor;
                endCursor = cursor + 140;
                req.setProxyId(privateData.substring(cursor, endCursor));
                
                cursor = endCursor;
                endCursor = cursor + 140;
                req.setProxyType(privateData.substring(cursor, endCursor));
            }
        }
        return req;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, RestResponse<BaseOutboundDTO> rr) {
        try {
            AccountEnquiryOutboundResponse accountEnquiryRsp = (AccountEnquiryOutboundResponse) rr.getContent().get(0);
            ISOMsg isoRsp = (ISOMsg) req.clone();
            isoRsp.setResponseMTI();
            isoRsp.set(39, "81");
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(accountEnquiryRsp.getNoRef(), 20))
                    .append(ISOUtil.strpad(rr.getResponseCode(), 4))
                    .append(ISOUtil.strpad(rr.getReasonCode(), 35))
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
            return isoRsp;
        } catch (ISOException ex) {
            return null;
        }
    }

}
