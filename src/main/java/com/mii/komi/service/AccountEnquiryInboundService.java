package com.mii.komi.service;

import com.mii.komi.dto.AccountEnquiryRequest;
import com.mii.komi.dto.AccountEnquiryResponse;
import com.mii.komi.dto.BaseRequestDTO;
import com.mii.komi.dto.BaseResponseDTO;
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
public class AccountEnquiryInboundService implements InboundService {
    
    @Autowired
    private ISO8583Service isoService;

    @Override
    public ISOMsg buildRequestMsg(BaseRequestDTO request) throws ISOException {
        AccountEnquiryRequest accountEnquiryRequest = (AccountEnquiryRequest)request;
        StringBuilder sb = new StringBuilder();
        String transactionId = ISOUtil.strpad(accountEnquiryRequest.getTransactionId(), 8);
        String recipientBank = ISOUtil.strpad(accountEnquiryRequest.getRecipientBank(), 8);
        String amount = ISOUtil.zeropad(String.valueOf(accountEnquiryRequest.getAmount()), 12);
        String categoryPurpose = ISOUtil.zeropad(accountEnquiryRequest.getCategoryPurpose(), 2);
        String accountNumber = ISOUtil.strpad(accountEnquiryRequest.getAccountNumber(), 35);
        sb.append(transactionId).append(recipientBank).append(amount).append(categoryPurpose).append(accountNumber);
        
        ISOMsg isoMsg = isoService.buildFinancialMsg(amount, transactionId, amount);
        isoMsg.set(62, sb.toString());
        return isoMsg;
    }

    @Override
    public BaseResponseDTO buildResponseMsg(ISOMsg isoMsg) {
        String privateData = isoMsg.getString(62);
        
        AccountEnquiryResponse rsp = new AccountEnquiryResponse();
        int cursor = 0;
        int endCursor = 4;
        rsp.setStatus(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 35;
        rsp.setReason(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 34;
        rsp.setAccountNumber(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 35;
        rsp.setAccountType(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 140;
        rsp.setCreditorName(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 35;
        rsp.setCreditorId(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 35;
        rsp.setCreditorType(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 35;
        rsp.setResidentStatus(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 35;
        rsp.setTownName(privateData.substring(cursor, endCursor));
        
        return rsp;
    }
    
}
