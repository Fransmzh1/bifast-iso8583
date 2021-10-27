package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.RestResponse;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public interface BaseOutboundParticipant {
    
    public Object buildRequestMsg(ISOMsg isoMsg) throws ISOException;
    
    public ISOMsg buildFailedResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> rr);
    
    public ISOMsg buildResponseMsg(ISOMsg req, ResponseEntity<RestResponse<BaseOutboundDTO>> dto) throws ISOException;
    
}
