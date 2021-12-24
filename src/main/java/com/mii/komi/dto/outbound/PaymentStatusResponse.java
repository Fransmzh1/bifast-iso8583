package com.mii.komi.dto.outbound;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class PaymentStatusResponse extends BaseOutboundDTO {
   
    @JsonProperty("TerminalId")
    private String terminalId;

    @JsonProperty("CategoryPurpose")
    private String categoryPurpose;

    @JsonProperty("DebtorName")
    private String debtorName;

    @JsonProperty("DebtorType")
    private String debtorType;

    @JsonProperty("DebtorId")
    private String debtorId;

    @JsonProperty("DebtorAccountNumber")
    private String debtorAccountNumber;

    @JsonProperty("DebtorAccountType")
    private String debtorAccountType;

    @JsonProperty("DebtorResidentialStatus")
    private String debtorResidentialStatus;

    @JsonProperty("DebtorTownName")
    private String debtorTownName;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("FeeTransfer")
    private String feeTransfer;

    @JsonProperty("RecipientBank")
    private String recipientBank;

    @JsonProperty("CreditorName")
    private String creditorName;

    @JsonProperty("CreditorType")
    private String creditorType;

    @JsonProperty("CreditorId")
    private String creditorId;

    @JsonProperty("CreditorAccountNumber")
    private String creditorAccountNumber;

    @JsonProperty("CreditorAccountType")
    private String creditorAccountType;

    @JsonProperty("CreditorResidentialStatus")
    private String creditorResidentialStatus;

    @JsonProperty("CreditorTownName")
    private String creditorTownName;

    @JsonProperty("CreditorProxyId")
    private String creditorProxyId;

    @JsonProperty("CreditorProxyType")
    private String creditorProxyType;

    @JsonProperty("PaymentInformation")
    private String paymentInformation;

    public String getTerminalId() {
        return Optional.ofNullable(terminalId).orElse("");
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCategoryPurpose() {
        return Optional.ofNullable(categoryPurpose).orElse("");
    }

    public void setCategoryPurpose(String categoryPurpose) {
        this.categoryPurpose = categoryPurpose;
    }

    public String getDebtorName() {
        return Optional.ofNullable(debtorName).orElse("");
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getDebtorType() {
        return Optional.ofNullable(debtorType).orElse("");
    }

    public void setDebtorType(String debtorType) {
        this.debtorType = debtorType;
    }

    public String getDebtorId() {
        return Optional.ofNullable(debtorId).orElse("");
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public String getDebtorAccountNumber() {
        return Optional.ofNullable(debtorAccountNumber).orElse("");
    }

    public void setDebtorAccountNumber(String debtorAccountNumber) {
        this.debtorAccountNumber = debtorAccountNumber;
    }

    public String getDebtorAccountType() {
        return Optional.ofNullable(debtorAccountType).orElse("");
    }

    public void setDebtorAccountType(String debtorAccountType) {
        this.debtorAccountType = debtorAccountType;
    }

    public String getDebtorResidentialStatus() {
        return Optional.ofNullable(debtorResidentialStatus).orElse("");
    }

    public void setDebtorResidentialStatus(String debtorResidentialStatus) {
        this.debtorResidentialStatus = debtorResidentialStatus;
    }

    public String getDebtorTownName() {
        return Optional.ofNullable(debtorTownName).orElse("");
    }

    public void setDebtorTownName(String debtorTownName) {
        this.debtorTownName = debtorTownName;
    }

    public String getAmount() {
        return Optional.ofNullable(amount).orElse("0.00");
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFeeTransfer() {
        return Optional.ofNullable(feeTransfer).orElse("0.00");
    }

    public void setFeeTransfer(String feeTransfer) {
        this.feeTransfer = feeTransfer;
    }

    public String getRecipientBank() {
        return Optional.ofNullable(recipientBank).orElse("");
    }

    public void setRecipientBank(String recipientBank) {
        this.recipientBank = recipientBank;
    }

    public String getCreditorName() {
        return Optional.ofNullable(creditorName).orElse("");
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorType() {
        return Optional.ofNullable(creditorType).orElse("");
    }

    public void setCreditorType(String creditorType) {
        this.creditorType = creditorType;
    }

    public String getCreditorId() {
        return Optional.ofNullable(creditorId).orElse("");
    }

    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    public String getCreditorAccountNumber() {
        return Optional.ofNullable(creditorAccountNumber).orElse("");
    }

    public void setCreditorAccountNumber(String creditorAccountNumber) {
        this.creditorAccountNumber = creditorAccountNumber;
    }

    public String getCreditorAccountType() {
        return Optional.ofNullable(creditorAccountType).orElse("");
    }

    public void setCreditorAccountType(String creditorAccountType) {
        this.creditorAccountType = creditorAccountType;
    }

    public String getCreditorResidentialStatus() {
        return Optional.ofNullable(creditorResidentialStatus).orElse("");
    }

    public void setCreditorResidentialStatus(String creditorResidentialStatus) {
        this.creditorResidentialStatus = creditorResidentialStatus;
    }

    public String getCreditorTownName() {
        return Optional.ofNullable(creditorTownName).orElse("");
    }

    public void setCreditorTownName(String creditorTownName) {
        this.creditorTownName = creditorTownName;
    }

    public String getCreditorProxyId() {
        return Optional.ofNullable(creditorProxyId).orElse("");
    }

    public void setCreditorProxyId(String creditorProxyId) {
        this.creditorProxyId = creditorProxyId;
    }

    public String getCreditorProxyType() {
        return Optional.ofNullable(creditorProxyType).orElse("");
    }

    public void setCreditorProxyType(String creditorProxyType) {
        this.creditorProxyType = creditorProxyType;
    }

    public String getPaymentInformation() {
        return Optional.ofNullable(paymentInformation).orElse("");
    }

    public void setPaymentInformation(String paymentInformation) {
        this.paymentInformation = paymentInformation;
    }
    
    
}
