package com.mii.komi.dto.inbound;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class CreditTransferInboundRequest extends BaseInboundRequestDTO {
    
    private String categoryPurpose;
    
    private String debtorType;
    
    private String debtorId;
    
    private String debtorAccountNumber;
    
    private String debtorAccountType;
    
    private String amount;
    
    private String feeTransfer;
    
    private String recipientBank;
    
    private String creditorType;
    
    private String creditorId;
    
    private String creditorAccountNumber;
    
    private String creditorAccountType;
    
    private String creditorProxyId;
    
    private String creditorProxyType;
    
    private String paymentInformation;

    /**
     * @return the categoryPurpose
     */
    public String getCategoryPurpose() {
        return categoryPurpose;
    }

    /**
     * @param categoryPurpose the categoryPurpose to set
     */
    public void setCategoryPurpose(String categoryPurpose) {
        this.categoryPurpose = categoryPurpose;
    }

    /**
     * @return the debtorType
     */
    public String getDebtorType() {
        return debtorType;
    }

    /**
     * @param debtorType the debtorType to set
     */
    public void setDebtorType(String debtorType) {
        this.debtorType = debtorType;
    }

    /**
     * @return the debtorId
     */
    public String getDebtorId() {
        return debtorId;
    }

    /**
     * @param debtorId the debtorId to set
     */
    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    /**
     * @return the debtorAccountNumber
     */
    public String getDebtorAccountNumber() {
        return debtorAccountNumber;
    }

    /**
     * @param debtorAccountNumber the debtorAccountNumber to set
     */
    public void setDebtorAccountNumber(String debtorAccountNumber) {
        this.debtorAccountNumber = debtorAccountNumber;
    }

    /**
     * @return the debtorAccountType
     */
    public String getDebtorAccountType() {
        return debtorAccountType;
    }

    /**
     * @param debtorAccountType the debtorAccountType to set
     */
    public void setDebtorAccountType(String debtorAccountType) {
        this.debtorAccountType = debtorAccountType;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the feeTransfer
     */
    public String getFeeTransfer() {
        return feeTransfer;
    }

    /**
     * @param feeTransfer the feeTransfer to set
     */
    public void setFeeTransfer(String feeTransfer) {
        this.feeTransfer = feeTransfer;
    }

    /**
     * @return the recipientBank
     */
    public String getRecipientBank() {
        return recipientBank;
    }

    /**
     * @param recipientBank the recipientBank to set
     */
    public void setRecipientBank(String recipientBank) {
        this.recipientBank = recipientBank;
    }

    /**
     * @return the creditorType
     */
    public String getCreditorType() {
        return creditorType;
    }

    /**
     * @param creditorType the creditorType to set
     */
    public void setCreditorType(String creditorType) {
        this.creditorType = creditorType;
    }

    /**
     * @return the creditorId
     */
    public String getCreditorId() {
        return creditorId;
    }

    /**
     * @param creditorId the creditorId to set
     */
    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    /**
     * @return the creditorAccountNumber
     */
    public String getCreditorAccountNumber() {
        return creditorAccountNumber;
    }

    /**
     * @param creditorAccountNumber the creditorAccountNumber to set
     */
    public void setCreditorAccountNumber(String creditorAccountNumber) {
        this.creditorAccountNumber = creditorAccountNumber;
    }

    /**
     * @return the creditorAccountType
     */
    public String getCreditorAccountType() {
        return creditorAccountType;
    }

    /**
     * @param creditorAccountType the creditorAccountType to set
     */
    public void setCreditorAccountType(String creditorAccountType) {
        this.creditorAccountType = creditorAccountType;
    }

    /**
     * @return the creditorProxyId
     */
    public String getCreditorProxyId() {
        return creditorProxyId;
    }

    /**
     * @param creditorProxyId the creditorProxyId to set
     */
    public void setCreditorProxyId(String creditorProxyId) {
        this.creditorProxyId = creditorProxyId;
    }

    /**
     * @return the creditorProxyType
     */
    public String getCreditorProxyType() {
        return creditorProxyType;
    }

    /**
     * @param creditorProxyType the creditorProxyType to set
     */
    public void setCreditorProxyType(String creditorProxyType) {
        this.creditorProxyType = creditorProxyType;
    }

    /**
     * @return the paymentInformation
     */
    public String getPaymentInformation() {
        return paymentInformation;
    }

    /**
     * @param paymentInformation the paymentInformation to set
     */
    public void setPaymentInformation(String paymentInformation) {
        this.paymentInformation = paymentInformation;
    }
    
}
