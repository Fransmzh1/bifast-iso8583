package com.mii.komi.dto.outbound.requestroot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mii.komi.dto.outbound.AccountEnquiryOutboundRequest;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RootAccountEnquiry extends BaseRootHttpRequest {
    
    @JsonProperty("AccountEnquiryRequest")
    private AccountEnquiryOutboundRequest accountEnquiryRequest;

    /**
     * @return the accountEnquiryRequest
     */
    public AccountEnquiryOutboundRequest getAccountEnquiryRequest() {
        return accountEnquiryRequest;
    }

    /**
     * @param accountEnquiryRequest the accountEnquiryRequest to set
     */
    public void setAccountEnquiryRequest(AccountEnquiryOutboundRequest accountEnquiryRequest) {
        this.accountEnquiryRequest = accountEnquiryRequest;
    }
    
}
