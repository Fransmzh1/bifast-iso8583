package com.mii.komi.dto.inbound;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class SettlementResponse extends BaseInboundRequestDTO {

    private String status;
    
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

}
