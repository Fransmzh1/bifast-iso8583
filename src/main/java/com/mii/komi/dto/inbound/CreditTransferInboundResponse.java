package com.mii.komi.dto.inbound;

import com.mii.komi.dto.BaseResponseDTO;


/**
 *
 * @author vinch
 */
public class CreditTransferInboundResponse extends BaseResponseDTO {
    
    private String additionalInfo;
    
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
