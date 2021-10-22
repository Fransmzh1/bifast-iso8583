package com.mii.komi.dto.outbound;

import java.io.Serializable;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class BaseOutboundDTO implements Serializable {
    
    private String noRef;

    /**
     * @return the noRef
     */
    public String getNoRef() {
        return noRef.trim();
    }

    /**
     * @param noRef the noRef to set
     */
    public void setNoRef(String noRef) {
        this.noRef = noRef;
    }
    
}
