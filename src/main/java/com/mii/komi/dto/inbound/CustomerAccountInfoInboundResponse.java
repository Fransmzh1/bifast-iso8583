package com.mii.komi.dto.inbound;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class CustomerAccountInfoInboundResponse extends BaseInboundResponseDTO {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> emailAddressList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> phoneNumberList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accountNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accountType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customerName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customerType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customerId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customerIdType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String residentStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String townName;

    public List<String> getEmailAddressList() {
        return emailAddressList;
    }

    public void setEmailAddressList(List<String> emailAddressList) {
        this.emailAddressList = emailAddressList;
    }

    public List<String> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerIdType() {
        return customerIdType;
    }

    public void setCustomerIdType(String customerIdType) {
        this.customerIdType = customerIdType;
    }

    public String getResidentStatus() {
        return residentStatus;
    }

    public void setResidentStatus(String residentStatus) {
        this.residentStatus = residentStatus;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    
}
