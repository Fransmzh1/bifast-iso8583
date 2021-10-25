package com.mii.komi.dto.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class BalanceInquiryResponse extends BaseInboundResponseDTO {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

}
