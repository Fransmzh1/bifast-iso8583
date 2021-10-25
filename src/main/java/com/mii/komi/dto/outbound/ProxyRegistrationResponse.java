package com.mii.komi.dto.outbound;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class ProxyRegistrationResponse extends BaseOutboundDTO {
    
    @JsonProperty("RegistrationType")
    private String registrationType;

    @JsonProperty("RegistrationId")
    private String registrationId;

    public String getRegistrationType() {
        return Optional.ofNullable(registrationType).orElse("");
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getRegistrationId() {
        return Optional.ofNullable(registrationId).orElse("");
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    
}
