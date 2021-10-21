package com.mii.komi.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class BaseRootHttpRequest {
    
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    
}
