package com.mii.komi.dto.inbound;

import java.util.Optional;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class DebitTransferInboundRequest extends BaseInboundRequestDTO {
    private String originalNoRef;
    private String originalDateTime;
    private String categoryPurpose;
    private String debtorName;
    private String debtorType;
    private String debtorId;
    private String debtorAccountNumber;
    private String debtorAccountType;
    private String debtorResidentStatus;
    private String debtorTownName;
    private String amount;
    private String feeTransfer;
    private String recipientBank;
    private String creditorName;
    private String creditorType;
    private String creditorId;
    private String creditorAccountNumber;
    private String creditorAccountType;
    private String creditorResidentStatus;
    private String creditorTownName;
    private String creditorProxyId;
    private String creditorProxyType;
    private String paymentInformation;

    public String getOriginalNoRef() {
        return Optional.ofNullable(originalNoRef).orElse("");        
    }

    public void setOriginalNoRef(String originalNoRef) {
        this.originalNoRef = originalNoRef;
    }

    public String getOriginalDateTime() {
        return Optional.ofNullable(originalDateTime).orElse("0");
    }

    public void setOriginalDateTime(String originalDateTime) {
        this.originalDateTime = originalDateTime;
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

    public String getDebtorResidentStatus() {
        return Optional.ofNullable(debtorResidentStatus).orElse("");
    }

    public void setDebtorResidentStatus(String debtorResidentStatus) {
        this.debtorResidentStatus = debtorResidentStatus;
    }

    public String getDebtorTownName() {
        return Optional.ofNullable(debtorTownName).orElse("");
    }

    public void setDebtorTownName(String debtorTownName) {
        this.debtorTownName = debtorTownName;
    }

    public String getAmount() {
        return Optional.ofNullable(amount).orElse("0");
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFeeTransfer() {
        return Optional.ofNullable(feeTransfer).orElse("0");
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

    public String getCreditorResidentStatus() {
        return Optional.ofNullable(creditorResidentStatus).orElse("");
    }

    public void setCreditorResidentStatus(String creditorResidentStatus) {
        this.creditorResidentStatus = creditorResidentStatus;
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
