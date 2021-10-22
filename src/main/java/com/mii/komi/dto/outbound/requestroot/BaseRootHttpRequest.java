package com.mii.komi.dto.outbound.requestroot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 * @param <T>
 */
public class BaseRootHttpRequest {
    
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    
}
