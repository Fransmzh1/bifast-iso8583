package com.mii.komi.dto.outbound;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class AccountEnquiryOutboundResponse extends BaseOutboundDTO {
    
    @JsonProperty("AccountNumber")
    private String accountNumber;
    
    @JsonProperty("ProxyType")
    private String proxyType;
    
    @JsonProperty("ProxyId")
    private String proxyId;
    
    @JsonProperty("AccountType")
    private String accountType;
    
    @JsonProperty("CreditorName")
    private String creditorName;
    
    @JsonProperty("CreditorId")
    private String creditorId;
    
    @JsonProperty("CreditorType")
    private String creditorType;
    
    @JsonProperty("ResidentStatus")
    private String residentStatus;
    
    @JsonProperty("TownName")
    private String townName;

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
     * @return the proxyType
     */
    public String getProxyType() {
        return Optional.ofNullable(proxyType).orElse("");
    }

    /**
     * @param proxyType the proxyType to set
     */
    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
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

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return Optional.ofNullable(accountType).orElse("");
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * @return the creditorName
     */
    public String getCreditorName() {
        return Optional.ofNullable(creditorName).orElse("");
    }

    /**
     * @param creditorName the creditorName to set
     */
    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    /**
     * @return the creditorId
     */
    public String getCreditorId() {
        return Optional.ofNullable(creditorId).orElse("");
    }

    /**
     * @param creditorId the creditorId to set
     */
    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    /**
     * @return the creditorType
     */
    public String getCreditorType() {
        return Optional.ofNullable(creditorType).orElse("");
    }

    /**
     * @param creditorType the creditorType to set
     */
    public void setCreditorType(String creditorType) {
        this.creditorType = creditorType;
    }

    /**
     * @return the residentStatus
     */
    public String getResidentStatus() {
        return Optional.ofNullable(residentStatus).orElse("");
    }

    /**
     * @param residentStatus the residentStatus to set
     */
    public void setResidentStatus(String residentStatus) {
        this.residentStatus = residentStatus;
    }

    /**
     * @return the townName
     */
    public String getTownName() {
        return Optional.ofNullable(townName).orElse("");
    }

    /**
     * @param townName the townName to set
     */
    public void setTownName(String townName) {
        this.townName = townName;
    }
    
}
