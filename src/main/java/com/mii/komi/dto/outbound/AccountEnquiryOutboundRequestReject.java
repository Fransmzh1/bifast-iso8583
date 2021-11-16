package com.mii.komi.dto.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountEnquiryOutboundRequestReject extends BaseOutboundDTO {
    @JsonProperty("AccountNumber")
    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
