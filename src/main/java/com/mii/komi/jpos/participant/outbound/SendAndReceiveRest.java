package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.AccountEnquiryRequest;
import com.mii.komi.dto.AccountEnquiryResponse;
import com.mii.komi.exception.RestTemplateResponseErrorHandler;
import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vinch
 */
public class SendAndReceiveREST implements TransactionParticipant {

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        AccountEnquiryRequest accountEnquiryRequest = ctx.get(Constants.HTTP_REQUEST);
        String endpointKomi = "http://demo8364822.mockable.io/komi-outbound/service/AccountEnquiryRequest";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        AccountEnquiryResponse accountEnquiryResponse = restTemplate.postForObject(
                endpointKomi,
                accountEnquiryRequest,
                AccountEnquiryResponse.class);
        ctx.put(Constants.HTTP_RESPONSE, accountEnquiryResponse);
        return PREPARED;
    }

}
