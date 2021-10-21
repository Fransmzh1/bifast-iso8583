package com.mii.komi.jpos.participant;

import com.mii.komi.dto.AccountEnquiryRequest;
import com.mii.komi.dto.AccountEnquiryResponse;
import com.mii.komi.dto.RestResponse;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.exception.RestTemplateResponseErrorHandler;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class AccountEnquiryOutboundParticipant implements TransactionParticipant, BaseOutboundParticipant {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    Environment env;

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        String endpointKomi = env.getProperty("endpoint.komi.url");
        try {
            AccountEnquiryRequest accountEnquiryRequest = buildRequestMsg(reqMsg);
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
            RestResponse<AccountEnquiryResponse> accountEnquiryResponse = restTemplate.postForObject(
                    endpointKomi,
                    accountEnquiryRequest,
                    RestResponse.class);
            ctx.put(Constants.HTTP_RESPONSE, accountEnquiryResponse);
            return PREPARED;
        } catch (DataNotFoundException ex) {
            ex.printStackTrace();
            return ABORTED;
        } catch (HttpRequestException ex) {
            ex.printStackTrace();
            return ABORTED;
        }
    }

    @Override
    public void commit(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg req = ctx.get(Constants.ISO_REQUEST);
        RestResponse<AccountEnquiryResponse> httpRsp = ctx.get(Constants.HTTP_RESPONSE);
        try {
            ISOMsg rsp = buildResponseMsg(req, httpRsp);
            ctx.put(Constants.ISO_RESPONSE, rsp);
        } catch (ISOException ex) {
            Logger.getLogger(AccountEnquiryOutboundParticipant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void abort(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg req = ctx.get(Constants.ISO_REQUEST);
        RestResponse<AccountEnquiryResponse> httpRsp = ctx.get(Constants.HTTP_RESPONSE);
        ISOMsg rsp = buildFailedResponseMsg(req, httpRsp);
        ctx.put(Constants.ISO_RESPONSE, rsp);
    }
    
    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, RestResponse response) throws ISOException {
        AccountEnquiryResponse accountEnquiryRsp = (AccountEnquiryResponse) response.getContent().get(0);
        ISOMsg isoRsp = (ISOMsg)req.clone();
        isoRsp.setResponseMTI();
        String status = response.getResponseCode();
        isoRsp.set(39, "00");
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(accountEnquiryRsp.getNoRef(), 20))
                .append(ISOUtil.strpad(response.getResponseCode(), 4))
                .append(ISOUtil.strpad(response.getResponseMessage(), 35))
                .append(ISOUtil.zeropad(accountEnquiryRsp.getAccountNumber(), 34))
                .append(ISOUtil.strpad(accountEnquiryRsp.getAccountType(), 35))
                .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorName(), 140))
                .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorId(), 35))
                .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorType(), 35))
                .append(ISOUtil.strpad(accountEnquiryRsp.getResidentStatus(), 35))
                .append(ISOUtil.strpad(accountEnquiryRsp.getTownName(), 35));
        isoRsp.set(62, sb.toString());
        return isoRsp;
        
    }

    @Override
    public AccountEnquiryRequest buildRequestMsg(ISOMsg isoMsg) {
        String privateData = isoMsg.getString(62);
        
        AccountEnquiryRequest req = new AccountEnquiryRequest();
        int cursor = 0;
        int endCursor = 20;
        req.setNoRef(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 8;
        req.setRecipientBank(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 12;
        req.setAmount(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 2;
        req.setCategoryPurpose(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 34;
        req.setAccountNumber(privateData.substring(cursor, endCursor));
        
        return req;
    }

    public ISOMsg buildFailedResponseMsg(ISOMsg req, RestResponse response) {
        try {
            AccountEnquiryResponse accountEnquiryRsp = (AccountEnquiryResponse) response.getContent().get(0);
            ISOMsg isoRsp = (ISOMsg)req.clone();
            isoRsp.setResponseMTI();
            String status = response.getResponseCode();
            isoRsp.set(39, "00");
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(accountEnquiryRsp.getNoRef(), 20))
                    .append(ISOUtil.strpad(response.getResponseCode(), 4))
                    .append(ISOUtil.strpad(response.getResponseMessage(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getAccountNumber(), 34))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getAccountType(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorName(), 140))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorId(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorType(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getResidentStatus(), 35))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getTownName(), 35));
            isoRsp.set(62, sb.toString());
            return isoRsp;
        } catch (ISOException ex) {
            return null;
        }
    }

}
