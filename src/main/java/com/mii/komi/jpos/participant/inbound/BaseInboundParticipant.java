package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.BaseInboundRequestDTO;
import com.mii.komi.dto.outbound.BaseOutboundDTO;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public interface BaseInboundParticipant {
    
    public ISOMsg buildRequestMsg(BaseInboundRequestDTO request) throws ISOException;
    
    public Object buildFailedResponseMsg(BaseInboundRequestDTO request, ISOMsg rsp);
    
    public Object buildResponseMsg(BaseInboundRequestDTO request, ISOMsg rsp);
    
}
