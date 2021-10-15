package com.mii.komi.jpos.participant;

import com.mii.komi.dto.AccountEnquiryRequest;
import com.mii.komi.dto.AccountEnquiryResponse;
import com.mii.komi.exception.RestTemplateResponseErrorHandler;
import com.mii.komi.service.AccountEnquiryOutboundService;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vinch
 */
public class AccountEnquiryOutboundParticipant implements TransactionParticipant {

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private AccountEnquiryOutboundService accountEnquiryService;
    
    @Autowired
    Environment env;
    
    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context)context;
        ISOMsg reqMsg = (ISOMsg)ctx.get(Constants.REQUEST_KEY);
        
        AccountEnquiryRequest accountEnquiryRequest = accountEnquiryService.buildRequestMsg(reqMsg);
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        restTemplate.postForObject(env.getProperty("endpoint.komi.url"), accountEnquiryRequest, AccountEnquiryResponse.class);
        
        return PREPARED;
    }

    @Override
    public void commit(long id, Serializable context) {}

    @Override
    public void abort(long id, Serializable context) {}
    
}
