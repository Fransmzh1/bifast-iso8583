package com.mii.komi.dto.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class ProxyRegistrationRequest extends BaseOutboundDTO {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("RegistrationType")
    private String registrationType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ProxyType")
    private String proxyType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ProxyValue")
    private String proxyValue;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("DisplayName")
    private String displayName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("AccountNumber")
    private String accountNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("AccountType")
    private String accountType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("AccountName")
    private String accountName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("SecondaryIdType")
    private String secondaryIdType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("SecondaryIdValue")
    private String secondaryIdValue;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CustomerType")
    private String customerType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("CustomerId")
    private String customerId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ResidentialStatus")
    private String residentialStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("TownName")
    private String townName;

    public String getRegistrationType() {
        return registrationType.trim();
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getProxyType() {
        return proxyType.trim();
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getProxyValue() {
        return proxyValue.trim();
    }

    public void setProxyValue(String proxyValue) {
        this.proxyValue = proxyValue;
    }

    public String getDisplayName() {
        return displayName.trim();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAccountNumber() {
        return accountNumber.trim();
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType.trim();
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName.trim();
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getSecondaryIdType() {
        return secondaryIdType.trim();
    }

    public void setSecondaryIdType(String secondaryIdType) {
        this.secondaryIdType = secondaryIdType;
    }

    public String getSecondaryIdValue() {
        return secondaryIdValue.trim();
    }

    public void setSecondaryIdValue(String secondaryIdValue) {
        this.secondaryIdValue = secondaryIdValue;
    }

    public String getCustomerType() {
        return customerType.trim();
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerId() {
        return customerId.trim();
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getResidentialStatus() {
        return residentialStatus.trim();
    }

    public void setResidentialStatus(String residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public String getTownName() {
        return townName.trim();
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    
}
