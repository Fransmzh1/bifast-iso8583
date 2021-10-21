package com.mii.komi.jpos.participant.outbound;

import com.mii.komi.dto.BaseResponseDTO;
import com.mii.komi.dto.RestResponse;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public interface BaseOutboundParticipant {
    
    public Object buildRequestMsg(ISOMsg isoMsg) throws ISOException;
    
    public ISOMsg buildFailedResponseMsg(ISOMsg req, BaseResponseDTO rr);
    
    public ISOMsg buildResponseMsg(ISOMsg req, BaseResponseDTO dto) throws ISOException;
    
}
