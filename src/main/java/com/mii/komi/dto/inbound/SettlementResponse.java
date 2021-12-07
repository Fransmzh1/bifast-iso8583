package com.mii.komi.dto.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class SettlementResponse extends BaseInboundResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String additionalInfo;

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

}
