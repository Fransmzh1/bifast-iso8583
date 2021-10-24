package com.mii.komi.dto.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class CreditTransferOutboundRequest extends BaseOutboundDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("TerminalId")
    private String terminalId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CategoryPurpose")
    private String categoryPurpose;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("DebtorName")
    private String debtorName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("DebtorType")
    private String debtorType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("DebtorId")
    private String debtorId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("DebtorAccountNumber")
    private String debtorAccountNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("DebtorAccountType")
    private String debtorAccountType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("DebtorResidentialStatus")
    private String debtorResidentialStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("DebtorTownName")
    private String debtorTownName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Amount")
    private String amount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("FeeTransfer")
    private String feeTransfer;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("RecipientBank")
    private String recipientBank;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CreditorName")
    private String creditorName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CreditorType")
    private String creditorType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CreditorId")
    private String creditorId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CreditorAccountNumber")
    private String creditorAccountNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CreditorAccountType")
    private String creditorAccountType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CreditorResidentialStatus")
    private String creditorResidentialStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CreditorTownName")
    private String creditorTownName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CreditorProxyId")
    private String creditorProxyId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CreditorProxyType")
    private String creditorProxyType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("PaymentInformation")
    private String paymentInformation;

    public String getTerminalId() {
        return terminalId.trim();
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCategoryPurpose() {
        return categoryPurpose.trim();
    }

    public void setCategoryPurpose(String categoryPurpose) {
        this.categoryPurpose = categoryPurpose;
    }

    public String getDebtorName() {
        return debtorName.trim();
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getDebtorType() {
        return debtorType.trim();
    }

    public void setDebtorType(String debtorType) {
        this.debtorType = debtorType;
    }

    public String getDebtorId() {
        return debtorId.trim();
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public String getDebtorAccountNumber() {
        return debtorAccountNumber.trim();
    }

    public void setDebtorAccountNumber(String debtorAccountNumber) {
        this.debtorAccountNumber = debtorAccountNumber;
    }

    public String getDebtorAccountType() {
        return debtorAccountType.trim();
    }

    public void setDebtorAccountType(String debtorAccountType) {
        this.debtorAccountType = debtorAccountType;
    }

    public String getDebtorResidentialStatus() {
        return debtorResidentialStatus.trim();
    }

    public void setDebtorResidentialStatus(String debtorResidentialStatus) {
        this.debtorResidentialStatus = debtorResidentialStatus;
    }

    public String getDebtorTownName() {
        return debtorTownName.trim();
    }

    public void setDebtorTownName(String debtorTownName) {
        this.debtorTownName = debtorTownName;
    }

    public String getAmount() {
        return amount.trim();
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFeeTransfer() {
        return feeTransfer.trim();
    }

    public void setFeeTransfer(String feeTransfer) {
        this.feeTransfer = feeTransfer;
    }

    public String getRecipientBank() {
        return recipientBank.trim();
    }

    public void setRecipientBank(String recipientBank) {
        this.recipientBank = recipientBank;
    }

    public String getCreditorName() {
        return creditorName.trim();
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorType() {
        return creditorType.trim();
    }

    public void setCreditorType(String creditorType) {
        this.creditorType = creditorType;
    }

    public String getCreditorId() {
        return creditorId.trim();
    }

    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    public String getCreditorAccountNumber() {
        return creditorAccountNumber.trim();
    }

    public void setCreditorAccountNumber(String creditorAccountNumber) {
        this.creditorAccountNumber = creditorAccountNumber;
    }

    public String getCreditorAccountType() {
        return creditorAccountType.trim();
    }

    public void setCreditorAccountType(String creditorAccountType) {
        this.creditorAccountType = creditorAccountType;
    }

    public String getCreditorResidentialStatus() {
        return creditorResidentialStatus.trim();
    }

    public void setCreditorResidentialStatus(String creditorResidentialStatus) {
        this.creditorResidentialStatus = creditorResidentialStatus;
    }

    public String getCreditorTownName() {
        return creditorTownName.trim();
    }

    public void setCreditorTownName(String creditorTownName) {
        this.creditorTownName = creditorTownName;
    }

    public String getCreditorProxyId() {
        return creditorProxyId.trim();
    }

    public void setCreditorProxyId(String creditorProxyId) {
        this.creditorProxyId = creditorProxyId;
    }

    public String getCreditorProxyType() {
        return creditorProxyType.trim();
    }

    public void setCreditorProxyType(String creditorProxyType) {
        this.creditorProxyType = creditorProxyType;
    }

    public String getPaymentInformation() {
        return paymentInformation.trim();
    }

    public void setPaymentInformation(String paymentInformation) {
        this.paymentInformation = paymentInformation;
    }

    


}
