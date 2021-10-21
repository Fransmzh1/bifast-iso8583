package com.mii.komi.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class BaseResponseDTO extends BaseRequestDTO {
    
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
    
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    
}
