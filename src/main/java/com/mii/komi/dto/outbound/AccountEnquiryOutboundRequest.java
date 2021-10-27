package com.mii.komi.dto.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class AccountEnquiryOutboundRequest extends BaseOutboundDTO {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("SenderAccountNumber")
    private String senderAccountNumber;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("RecipientBank")
    private String recipientBank;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("RecipientAccountNumber")
    private String recipientAccountNumber;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Amount")
    private String amount;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CategoryPurpose")
    private String categoryPurpose;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ProxyId")
    private String proxyId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ProxyType")
    private String proxyType;

    /**
     * @return the senderAccountNumber
     */
    public String getSenderAccountNumber() {
        return senderAccountNumber.trim();
    }

    /**
     * @param senderAccountNumber the senderAccountNumber to set
     */
    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    /**
     * @return the recipientBank
     */
    public String getRecipientBank() {
        return recipientBank.trim();
    }

    /**
     * @param recipientBank the recipientBank to set
     */
    public void setRecipientBank(String recipientBank) {
        this.recipientBank = recipientBank;
    }

    /**
     * @return the recipientAccountNumber
     */
    public String getRecipientAccountNumber() {
        return recipientAccountNumber.trim();
    }

    /**
     * @param recipientAccountNumber the recipientAccountNumber to set
     */
    public void setRecipientAccountNumber(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount.trim();
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the categoryPurpose
     */
    public String getCategoryPurpose() {
        return categoryPurpose.trim();
    }

    /**
     * @param categoryPurpose the categoryPurpose to set
     */
    public void setCategoryPurpose(String categoryPurpose) {
        this.categoryPurpose = categoryPurpose;
    }

    /**
     * @return the proxyId
     */
    public String getProxyId() {
        if(proxyId != null) {
        return proxyId.trim();
        } else {
            return null;
        }
    }

    /**
     * @param proxyId the proxyId to set
     */
    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }

    /**
     * @return the proxyType
     */
    public String getProxyType() {
        if(proxyType != null) {
        return proxyType.trim();
        } else {
            return null;
        }
    }

    /**
     * @param proxyType the proxyType to set
     */
    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }
    
}
