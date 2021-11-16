package com.mii.komi.dto.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mii.komi.dto.BaseDTO;
import com.mii.komi.util.Constants;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RestResponse<T> extends BaseDTO {
    
    @JsonProperty("Date")
    private String date;
    
    @JsonProperty("Time")
    private String time;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ReasonCode")
    private String reasonCode;

    @JsonProperty("ReasonMessage")
    private String reasonMessage;

    @JsonProperty("Content")
    private List<T> content;
    
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    
    public RestResponse() {}
    
    public RestResponse(List<T> content) {
        this.content = content;
    }
    
    public static RestResponse success(String message, List o) {
        RestResponse rr = new RestResponse(o);
        rr.setDate(dateFormatter.format(LocalDate.now()));
        rr.setTime(timeFormatter.format(LocalTime.now()));
        rr.setReasonMessage(message);
        rr.setReasonCode(Constants.REASON_CODE_SUCCESS);
        rr.setResponseCode(Constants.RESPONSE_CODE_ACCEPTED);
        rr.setContent(o);
        return rr;
    }
    
    public static RestResponse success(String message) {
        RestResponse rr = new RestResponse(new ArrayList<>());
        rr.setDate(dateFormatter.format(LocalDate.now()));
        rr.setTime(timeFormatter.format(LocalTime.now()));
        rr.setReasonMessage(message);
        rr.setReasonCode(Constants.REASON_CODE_SUCCESS);
        rr.setResponseCode(Constants.RESPONSE_CODE_ACCEPTED);
        return rr;
    }
    
    public static RestResponse failed(String reasonCode, String message, String responseCode) {
        RestResponse rr = new RestResponse(new ArrayList<>());
        rr.setDate(dateFormatter.format(LocalDate.now()));
        rr.setTime(timeFormatter.format(LocalTime.now()));
        rr.setReasonCode(reasonCode);
        rr.setReasonMessage(message);
        rr.setResponseCode(responseCode);
        return rr;

    }public static RestResponse failed(String reasonCode, String message, String responseCode, List o) {
        RestResponse rr = new RestResponse(new ArrayList<>());
        rr.setDate(dateFormatter.format(LocalDate.now()));
        rr.setTime(timeFormatter.format(LocalTime.now()));
        rr.setReasonCode(reasonCode);
        rr.setReasonMessage(message);
        rr.setResponseCode(responseCode);
        rr.setContent(o);
        return rr;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the responseCode
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the content
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(List<T> content) {
        this.content = content;
    }

    /**
     * @return the reasonCode
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * @param reasonCode the reasonCode to set
     */
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * @return the reasonMessage
     */
    public String getReasonMessage() {
        return reasonMessage;
    }

    /**
     * @param reasonMessage the reasonMessage to set
     */
    public void setReasonMessage(String reasonMessage) {
        this.reasonMessage = reasonMessage;
    }
    
}
