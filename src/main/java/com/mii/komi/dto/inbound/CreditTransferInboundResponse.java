package com.mii.komi.dto.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class CreditTransferInboundResponse extends BaseInboundResponseDTO {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String additionalInfo;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accountNumber;

    /**
     * @return the additionalInfo
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * @param additionalInfo the additionalInfo to set
     */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
}
