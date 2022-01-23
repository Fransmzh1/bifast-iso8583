package com.mii.komi.dto.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class ProxyResolutionRequest extends BaseOutboundDTO {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("SenderAccountNumber")
    private String senderAccountNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ProxyType")
    private String proxyType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ProxyValue")
    private String proxyValue;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("LookUpType")
    private String lookupType;

    public String getSenderAccountNumber() {
        return senderAccountNumber.trim();
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
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

    public String getLookupType() {
        return lookupType.trim();
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }
    
}
