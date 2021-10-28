package com.mii.komi.dto.outbound.requestroot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mii.komi.dto.outbound.CreditTransferOutboundRequest;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RootCreditTransfer extends BaseRootHttpRequest {
    
    @JsonProperty("CreditTransferRequest")
    private CreditTransferOutboundRequest creditTransferRequest;

    /**
     * @return the creditTransferRequest
     */
    public CreditTransferOutboundRequest getCreditTransferRequest() {
        return creditTransferRequest;
    }

    /**
     * @param creditTransferRequest the creditTransferRequest to set
     */
    public void setCreditTransferRequest(CreditTransferOutboundRequest creditTransferRequest) {
        this.creditTransferRequest = creditTransferRequest;
    }
    
    
    
}
