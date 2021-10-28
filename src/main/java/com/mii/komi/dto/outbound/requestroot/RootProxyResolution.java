package com.mii.komi.dto.outbound.requestroot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mii.komi.dto.outbound.ProxyResolutionRequest;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RootProxyResolution extends BaseRootHttpRequest {
    
    @JsonProperty("ProxyResolutionRequest")
    private ProxyResolutionRequest proxyResolutionRequest;

    public ProxyResolutionRequest getProxyResolutionRequest() {
        return proxyResolutionRequest;
    }

    public void setProxyResolutionRequest(ProxyResolutionRequest proxyResolutionRequest) {
        this.proxyResolutionRequest = proxyResolutionRequest;
    }

    
}
