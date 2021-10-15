package com.mii.komi.service;

import com.mii.komi.dto.BaseRequestDTO;
import com.mii.komi.dto.BaseResponseDTO;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 *
 * @author vinch
 */
public interface OutboundService {
    
    ISOMsg buildResponseMsg(BaseResponseDTO request) throws ISOException;
    
    BaseRequestDTO buildRequestMsg(ISOMsg isoMsg);
    
}
