package com.mii.komi.dto;

import java.util.Optional;

/**
 *
 * @author Erwin
 */
public class AccountEnquiryRequest extends BaseRequestDTO {
    
    private String channel;
    
    private String recipientBank;
    
    private String amount;
    
    private String categoryPurpose;
    
    private String accountNumber;

    /**
     * @return the channel
     */
    public String getChannel() {
        return Optional.ofNullable(channel).orElse("");
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

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
    
}
