package com.mii.komi.util;

import java.util.UUID;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class Utility {
    
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    
}
