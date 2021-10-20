package com.mii.komi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

/**
 *
 * @author Erwin
 */
public class AccountEnquiryRequest extends BaseRequestDTO {
    
    @JsonProperty("RecipientBank")
    private String recipientBank;
    
    @JsonProperty("Amount")
    private String amount;
    
    @JsonProperty("CategoryPurpose")
    private String categoryPurpose;
    
    @JsonProperty("AccountNumber")
    private String accountNumber;
    
    @JsonProperty("ProxyId")
    private String proxyId;

    /**
     * @return the recipientBank
     */
    public String getRecipientBank() {
        return Optional.ofNullable(recipientBank).orElse("");
    }

    /**
     * @param recipientBank the recipientBank to set
     */
    public void setRecipientBank(String recipientBank) {
        this.recipientBank = recipientBank;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return Optional.ofNullable(amount).orElse("0");
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
        return Optional.ofNullable(categoryPurpose).orElse("00");
    }

    /**
     * @param categoryPurpose the categoryPurpose to set
     */
    public void setCategoryPurpose(String categoryPurpose) {
        this.categoryPurpose = categoryPurpose;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return Optional.ofNullable(accountNumber).orElse("");
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the proxyId
     */
    public String getProxyId() {
        return Optional.ofNullable(proxyId).orElse("");
    }

    /**
     * @param proxyId the proxyId to set
     */
    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }
    
}
