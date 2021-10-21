package com.mii.komi.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ErwinSn
 */
public class RestResponse<T> {
    
    private String date;
    private String time;
    private String responseCode;
    private String responseMessage;
    private List<T> content;
    
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    
    public RestResponse(List<T> content) {
        this.content = content;
    }
    
    public static RestResponse success(String message, List o) {
        RestResponse rr = new RestResponse(o);
        rr.setDate(dateFormatter.format(LocalDate.now()));
        rr.setTime(timeFormatter.format(LocalTime.now()));
        rr.setResponseMessage(message);
        rr.setResponseCode("U000");
        rr.setContent(o);
        return rr;
    }
    
    public static RestResponse success(List o) {
        return RestResponse.success("Success", o);
    }
    
    public static RestResponse success(String message) {
        RestResponse rr = new RestResponse(new ArrayList<>());
        rr.setDate(dateFormatter.format(LocalDate.now()));
        rr.setTime(timeFormatter.format(LocalTime.now()));
        rr.setResponseMessage(message);
        rr.setResponseCode("U000");
        return rr;
    }
    
    public static RestResponse failed(String message, String responseCode) {
        RestResponse rr = new RestResponse(new ArrayList<>());
        rr.setDate(dateFormatter.format(LocalDate.now()));
        rr.setTime(timeFormatter.format(LocalTime.now()));
        rr.setResponseMessage(message);
        rr.setResponseCode(responseCode);
        return rr;
    }
    
    public static RestResponse failed(int code) {
        return RestResponse.failed(code);
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
     * @return the responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * @param responseMessage the responseMessage to set
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
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
    
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    
}
