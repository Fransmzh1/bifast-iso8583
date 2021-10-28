package com.mii.komi.dto.outbound.requestroot;

import com.mii.komi.dto.outbound.ProxyRegistrationRequest;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RootProxyRegistration extends BaseRootHttpRequest {
    
    private ProxyRegistrationRequest proxyRegistrationRequest;

    /**
     * @return the proxyRegistrationRequest
     */
    public ProxyRegistrationRequest getProxyRegistrationRequest() {
        return proxyRegistrationRequest;
    }

    /**
     * @param proxyRegistrationRequest the proxyRegistrationRequest to set
     */
    public void setProxyRegistrationRequest(ProxyRegistrationRequest proxyRegistrationRequest) {
        this.proxyRegistrationRequest = proxyRegistrationRequest;
    }
}
