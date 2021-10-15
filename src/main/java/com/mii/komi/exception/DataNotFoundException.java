package com.mii.komi.exception;

/**
 *
 * @author ErwinSn
 */
public class DataNotFoundException extends RuntimeException {
    
    public DataNotFoundException() {
        super("Data not found");
    }
    
    public DataNotFoundException(Long id) {
        super("Data with id '" + id + "' not found");
    }
    
    public DataNotFoundException(String id) {
        super("Data '" + id + "' not found");
    }
    
}
