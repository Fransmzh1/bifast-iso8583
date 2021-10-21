package com.mii.komi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Optional;

/**
 *
 * @author vinch
 */
public class BaseRequestDTO {
    
    @JsonProperty("NoRef")
    private String noRef;
    
    private String dateTime;

    /**
     * @return the noRef
     */
    public String getNoRef() {
        return Optional.ofNullable(noRef).orElse("");
    }

    /**
     * @param noRef the noRef to set
     */
    public void setNoRef(String noRef) {
        this.noRef = noRef;
    }

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return Optional.ofNullable(dateTime).orElse("");
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
}
