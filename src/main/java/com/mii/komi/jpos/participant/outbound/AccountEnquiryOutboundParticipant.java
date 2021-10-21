package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.AccountEnquiryRequest;
import com.mii.komi.dto.AccountEnquiryResponse;
import com.mii.komi.dto.BaseResponseDTO;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.exception.RestTemplateResponseErrorHandler;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class AccountEnquiryOutboundParticipant implements TransactionParticipant, BaseOutboundParticipant, Configurable {

    private Configuration cfg;
    
    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            AccountEnquiryRequest accountEnquiryRequest = buildRequestMsg(reqMsg);
            ctx.put(Constants.HTTP_REQUEST, accountEnquiryRequest);
            String endpointKomi = cfg.get("endpoint");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
            AccountEnquiryResponse accountEnquiryResponse = restTemplate.postForObject(
                    endpointKomi,
                    accountEnquiryRequest,
                    AccountEnquiryResponse.class);
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
        AccountEnquiryResponse httpRsp = ctx.get(Constants.HTTP_RESPONSE);
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
        AccountEnquiryResponse httpRsp = ctx.get(Constants.HTTP_RESPONSE);
        ISOMsg rsp = buildFailedResponseMsg(req, httpRsp);
        ctx.put(Constants.ISO_RESPONSE, rsp);
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, BaseResponseDTO response) throws ISOException {
        AccountEnquiryResponse accountEnquiryRsp = (AccountEnquiryResponse) response;
        ISOMsg isoRsp = (ISOMsg) req.clone();
        isoRsp.setResponseMTI();
        isoRsp.set(39, "00");
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(accountEnquiryRsp.getNoRef(), 20))
                .append(ISOUtil.strpad(accountEnquiryRsp.getStatus(), 4))
                .append(ISOUtil.strpad(accountEnquiryRsp.getReason(), 35))
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
        String privateData = isoMsg.getString(48);

        AccountEnquiryRequest req = new AccountEnquiryRequest();
        int cursor = 0;
        int endCursor = 20;
        req.setNoRef(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 8;
        req.setRecipientBank(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 12;
        req.setAmount(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 2;
        req.setCategoryPurpose(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 34;
        req.setAccountNumber(privateData.substring(cursor, endCursor));

        return req;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, BaseResponseDTO rr) {
        try {
            AccountEnquiryResponse accountEnquiryRsp = (AccountEnquiryResponse) rr;
            ISOMsg isoRsp = (ISOMsg) req.clone();
            isoRsp.setResponseMTI();
            isoRsp.set(39, "00");
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(accountEnquiryRsp.getNoRef(), 20))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getStatus(), 4))
                    .append(ISOUtil.strpad(accountEnquiryRsp.getReason(), 35))
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

    @Override
    public void setConfiguration(Configuration c) throws ConfigurationException {
        this.cfg = c;
    }

}
