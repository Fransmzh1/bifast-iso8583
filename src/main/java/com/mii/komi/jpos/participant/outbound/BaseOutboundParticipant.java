package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.RestResponse;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public interface BaseOutboundParticipant {
    
    public Object buildRequestMsg(ISOMsg isoMsg) throws ISOException;
    
    public ISOMsg buildFailedResponseMsg(ISOMsg req, RestResponse<BaseOutboundDTO> rr);
    
    public ISOMsg buildResponseMsg(ISOMsg req, RestResponse<BaseOutboundDTO> dto) throws ISOException;
    
}
