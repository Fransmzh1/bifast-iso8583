package com.mii.komi.dto.inbound;

import java.util.Optional;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class CustomerAccountInfoInboundRequest extends BaseInboundRequestDTO {
    
    private String accountNumber;

    public String getAccountNumber() {
        return Optional.ofNullable(accountNumber).orElse("");
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    
}
