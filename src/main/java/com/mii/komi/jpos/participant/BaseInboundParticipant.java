package com.mii.komi.jpos.participant;

import com.mii.komi.dto.BaseRequestDTO;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public interface BaseInboundParticipant {
    
    public ISOMsg buildRequestMsg(BaseRequestDTO request) throws ISOException;
    
    public Object buildFailedResponseMsg(BaseRequestDTO request, ISOMsg rsp);
    
    public Object buildResponseMsg(BaseRequestDTO request, ISOMsg rsp);
    
}
