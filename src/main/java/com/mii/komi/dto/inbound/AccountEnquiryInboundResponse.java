package com.mii.komi.dto.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class AccountEnquiryInboundResponse extends BaseInboundResponseDTO {

    @JsonInclude(Include.NON_NULL)
    private String accountNumber;
    
    @JsonInclude(Include.NON_NULL)
    private String accountType;
    
    @JsonInclude(Include.NON_NULL)
    private String creditorName;
    
    @JsonInclude(Include.NON_NULL)
    private String creditorId;
    
    @JsonInclude(Include.NON_NULL)
    private String creditorType;
    
    @JsonInclude(Include.NON_NULL)
    private String residentStatus;
    
    @JsonInclude(Include.NON_NULL)
    private String townName;

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
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
        return creditorName;
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
        return creditorId;
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
        return creditorType;
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
        return residentStatus;
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
        return townName;
    }

    /**
     * @param townName the townName to set
     */
    public void setTownName(String townName) {
        this.townName = townName;
    }
    
}
