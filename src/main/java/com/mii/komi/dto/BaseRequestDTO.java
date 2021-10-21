package com.mii.komi.dto;

import java.io.Serializable;

/**
 *
 * @author vinch
 */
public class BaseRequestDTO implements Serializable {
    
    private String transactionId;
    
    private String noRef;
    
    private String merchantType;
    
    private String terminalId;
    
    private String dateTime;

    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return the noRef
     */
    public String getNoRef() {
        return noRef;
    }

    /**
     * @param noRef the noRef to set
     */
    public void setNoRef(String noRef) {
        this.noRef = noRef;
    }

    /**
     * @return the merchantType
     */
    public String getMerchantType() {
        return merchantType;
    }

    /**
     * @param merchantType the merchantType to set
     */
    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    /**
     * @return the terminalId
     */
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * @param terminalId the terminalId to set
     */
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
}
