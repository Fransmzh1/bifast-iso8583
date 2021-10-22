package com.mii.komi.dto.inbound;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class BaseInboundResponseDTO extends BaseInboundRequestDTO {
    
    private String status;
    
    private String reason;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    
}
