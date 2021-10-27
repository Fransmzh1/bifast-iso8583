package com.mii.komi.dto.outbound.requestroot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mii.komi.dto.outbound.CreditTransferOutboundRequest;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RootCreditTransfer extends BaseRootHttpRequest {
    
    @JsonProperty("CreditTransferRequest")
    private CreditTransferOutboundRequest creditTransferOutboundRequest;

    public CreditTransferOutboundRequest getCreditTransferOutboundRequest() {
        return creditTransferOutboundRequest;
    }

    public void setCreditTransferOutboundRequest(CreditTransferOutboundRequest creditTransferOutboundRequest) {
        this.creditTransferOutboundRequest = creditTransferOutboundRequest;
    }

}
