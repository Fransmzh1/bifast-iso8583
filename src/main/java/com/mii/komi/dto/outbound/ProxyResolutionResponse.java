package com.mii.komi.dto.outbound;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class ProxyResolutionResponse extends BaseOutboundDTO {
    
    @JsonProperty("ProxyType")
    private String proxyType;

    @JsonProperty("ProxyValue")
    private String proxyValue;

    @JsonProperty("RegistrationId")
    private String registrationId;

    @JsonProperty("DisplayName")
    private String displayName;

    @JsonProperty("RegisterBank")
    private String registerBank;

    @JsonProperty("AccountNumber")
    private String accountNumber;

    @JsonProperty("AccountType")
    private String accountType;

    @JsonProperty("AccountName")
    private String accountName;

    @JsonProperty("CustomerType")
    private String customerType;

    @JsonProperty("CustomerId")
    private String customerId;

    @JsonProperty("ResidentialStatus")
    private String residentialStatus;

    @JsonProperty("TownName")
    private String townName;

    public String getProxyType() {
        return Optional.ofNullable(proxyType).orElse("");
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getProxyValue() {
        return Optional.ofNullable(proxyValue).orElse("");
    }

    public void setProxyValue(String proxyValue) {
        this.proxyValue = proxyValue;
    }

    public String getRegistrationId() {
        return Optional.ofNullable(registrationId).orElse("");
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getDisplayName() {
        return Optional.ofNullable(displayName).orElse("");
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRegisterBank() {
        return Optional.ofNullable(registerBank).orElse("");
    }

    public void setRegisterBank(String registerBank) {
        this.registerBank = registerBank;
    }

    public String getAccountNumber() {
        return Optional.ofNullable(accountNumber).orElse("");
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return Optional.ofNullable(accountType).orElse("");
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return Optional.ofNullable(accountName).orElse("");
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCustomerType() {
        return Optional.ofNullable(customerType).orElse("");
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerId() {
        return Optional.ofNullable(customerId).orElse("");
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getResidentialStatus() {
        return Optional.ofNullable(residentialStatus).orElse("");
    }

    public void setResidentialStatus(String residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public String getTownName() {
        return Optional.ofNullable(townName).orElse("");
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    

}
