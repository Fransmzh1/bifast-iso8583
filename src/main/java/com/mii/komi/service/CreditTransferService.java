package com.mii.komi.service;

import com.mii.komi.dto.BaseRequestDTO;
import com.mii.komi.dto.BaseResponseDTO;
import com.mii.komi.dto.inbound.CreditTransferInboundRequest;
import com.mii.komi.dto.inbound.CreditTransferInboundResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author vinch
 */
@Service
public class CreditTransferService implements InboundService {

    @Autowired
    private ISO8583Service isoService;
    
    @Override
    public ISOMsg buildRequestMsg(BaseRequestDTO requestMsg) throws ISOException {
        CreditTransferInboundRequest creditTransferRequest = (CreditTransferInboundRequest) requestMsg;
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(creditTransferRequest.getCategoryPurpose(), 2))
                .append(ISOUtil.zeropad(creditTransferRequest.getAmount(), 19))
                .append(ISOUtil.zeropad(creditTransferRequest.getCreditorType(), 35))
                .append(ISOUtil.strpad(creditTransferRequest.getCreditorId(), 35))
                .append(ISOUtil.strpad(creditTransferRequest.getCreditorAccountNumber(), 34))
                .append(ISOUtil.strpad(creditTransferRequest.getCreditorAccountType(), 35))
                .append(ISOUtil.strpad(creditTransferRequest.getCreditorProxyId(), 140))
                .append(ISOUtil.strpad(creditTransferRequest.getCreditorProxyType(), 35))
                .append(ISOUtil.strpad(creditTransferRequest.getPaymentInformation(), 140));
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy-MM-DD'T'HH:mm:ss.sss");
        LocalDateTime dateTime = LocalDateTime.parse(requestMsg.getDateTime(), dt);
        
        DateTimeFormatter dt2 = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String destinationDateTime = dt2.format(dateTime);
        
        ISOMsg iso = isoService.buildFinancialMsg(
                "389100", requestMsg.getNoRef(), "000");
        iso.set(48, sb.toString());
        return iso;
    }

    @Override
    public BaseResponseDTO buildResponseMsg(ISOMsg isoMsg) {
        String privateData = isoMsg.getString(62);
        
        CreditTransferInboundResponse rsp = new CreditTransferInboundResponse();
        int cursor = 0;
        int endCursor = 4;
        //rsp.setResponseCode(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 35;
        //rsp.setResponseMessage(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 140;
        rsp.setAdditionalInfo(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        rsp.setAccountNumber(privateData.substring(cursor));
        
        return rsp;
    }
    
}
