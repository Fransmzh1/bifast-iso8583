package com.mii.komi.jpos.participant.inbound;

import java.io.Serializable;
import org.jpos.iso.ISOMsg;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public interface BaseInboundParticipant {
    
    public ISOMsg buildRequestMsg(long id, Serializable context);
    
    public ResponseEntity buildFailedResponseMsg(long id, Serializable context);
    
    public ResponseEntity buildResponseMsg(long id, Serializable context);
    
    ResponseEntity buildSpesificRspBody(long id, Serializable context);
    
}
