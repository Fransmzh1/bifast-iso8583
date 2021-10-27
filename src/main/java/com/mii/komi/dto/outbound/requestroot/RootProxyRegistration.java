package com.mii.komi.dto.outbound.requestroot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mii.komi.dto.outbound.ProxyRegistrationRequest;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RootProxyRegistration extends BaseRootHttpRequest {
    
    @JsonProperty("ProxyRegistrationRequest")
    private ProxyRegistrationRequest proxyRegistrationRequest;

    public ProxyRegistrationRequest getProxyRegistrationRequest() {
        return proxyRegistrationRequest;
    }

    public void setProxyRegistrationRequest(ProxyRegistrationRequest proxyRegistrationRequest) {
        this.proxyRegistrationRequest = proxyRegistrationRequest;
    }

}
