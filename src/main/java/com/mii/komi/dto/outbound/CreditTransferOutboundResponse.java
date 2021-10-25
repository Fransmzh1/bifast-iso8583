package com.mii.komi.dto.outbound;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class CreditTransferOutboundResponse extends BaseOutboundDTO {
    
    @JsonProperty("AccountNumber")
    private String accountNumber;

    @JsonProperty("CreditorName")
    private String creditorName;

    public String getAccountNumber() {
        return Optional.ofNullable(accountNumber).orElse("");
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCreditorName() {
        return Optional.ofNullable(creditorName).orElse("");
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    
}
