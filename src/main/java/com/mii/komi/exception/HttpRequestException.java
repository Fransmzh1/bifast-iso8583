package com.mii.komi.exception;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class HttpRequestException extends RuntimeException {
    
    public HttpRequestException() {
        super("Request Error");
    }
    
    public HttpRequestException(String responseBody) {
        super(responseBody);
    }
    
}
