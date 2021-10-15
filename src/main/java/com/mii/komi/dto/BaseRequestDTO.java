package com.mii.komi.dto;

import java.util.Optional;

/**
 *
 * @author vinch
 */
public class BaseRequestDTO {
    
    private String transactionId;
    
    private String dateTime;

    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return Optional.ofNullable(transactionId).orElse("");
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return Optional.ofNullable(dateTime).orElse("");
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
}
