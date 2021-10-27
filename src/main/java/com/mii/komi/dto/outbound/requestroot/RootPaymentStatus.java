package com.mii.komi.dto.outbound.requestroot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mii.komi.dto.outbound.PaymentStatusRequest;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RootPaymentStatus extends BaseRootHttpRequest {
    
    @JsonProperty(value = "PaymentStatusRequest")
    private PaymentStatusRequest paymentStatusRequest;

    /**
     * @return the paymentStatusRequest
     */
    public PaymentStatusRequest getPaymentStatusRequest() {
        return paymentStatusRequest;
    }

    /**
     * @param paymentStatusRequest the paymentStatusRequest to set
     */
    public void setPaymentStatusRequest(PaymentStatusRequest paymentStatusRequest) {
        this.paymentStatusRequest = paymentStatusRequest;
    }
    
}
