package com.mii.komi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author vinch
 */
public class RootAccountEnquiryRequest {
    
    @JsonProperty("AccountEnquiryRequest")
    private AccountEnquiryRequest accountEnquiryRequest;

    /**
     * @return the accountEnquiryRequest
     */
    public AccountEnquiryRequest getAccountEnquiryRequest() {
        return accountEnquiryRequest;
    }

    /**
     * @param accountEnquiryRequest the accountEnquiryRequest to set
     */
    public void setAccountEnquiryRequest(AccountEnquiryRequest accountEnquiryRequest) {
        this.accountEnquiryRequest = accountEnquiryRequest;
    }
    
}
