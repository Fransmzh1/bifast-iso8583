package com.mii.komi.dto.outbound.requestroot;

import com.mii.komi.dto.inbound.AccountEnquiryInboundRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RootAccountEnquiry extends BaseRootHttpRequest {
    
    @JsonProperty("AccountEnquiryRequest")
    private AccountEnquiryInboundRequest accountEnquiryRequest;

    /**
     * @return the accountEnquiryRequest
     */
    public AccountEnquiryInboundRequest getAccountEnquiryRequest() {
        return accountEnquiryRequest;
    }

    /**
     * @param accountEnquiryRequest the accountEnquiryRequest to set
     */
    public void setAccountEnquiryRequest(AccountEnquiryInboundRequest accountEnquiryRequest) {
        this.accountEnquiryRequest = accountEnquiryRequest;
    }
    
}
