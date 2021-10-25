package com.mii.komi.dto.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class PaymentStatusRequest extends BaseOutboundDTO {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("OriginalNoRef")
    private String originalNoRef;

    public String getOriginalNoRef() {
        return originalNoRef.trim();
    }

    public void setOriginalNoRef(String originalNoRef) {
        this.originalNoRef = originalNoRef;
    }

    
}
