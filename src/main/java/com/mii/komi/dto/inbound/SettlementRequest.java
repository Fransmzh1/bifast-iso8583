package com.mii.komi.dto.inbound;

import java.util.Optional;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class SettlementRequest extends BaseInboundRequestDTO {
    
    private String originalNoRef;

    private String additionalInfo;

    public String getOriginalNoRef() {
        return Optional.ofNullable(originalNoRef).orElse("");
    }

    public void setOriginalNoRef(String originalNoRef) {
        this.originalNoRef = originalNoRef;
    }

    public String getAdditionalInfo() {
        return Optional.ofNullable(additionalInfo).orElse("");
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    
}
