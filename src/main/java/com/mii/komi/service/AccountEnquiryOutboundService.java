package com.mii.komi.service;

import com.mii.komi.dto.AccountEnquiryRequest;
import com.mii.komi.dto.AccountEnquiryResponse;
import com.mii.komi.dto.BaseResponseDTO;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

/**
 *
 * @author vinch
 */
public class AccountEnquiryOutboundService implements OutboundService {

    @Override
    public ISOMsg buildResponseMsg(BaseResponseDTO response) throws ISOException {
        AccountEnquiryResponse accountEnquiryRsp = (AccountEnquiryResponse) response;
        
        ISOMsg isoRsp = new ISOMsg();
        isoRsp.setResponseMTI();
        
        String status = accountEnquiryRsp.getStatus();
        if("ACPT".equalsIgnoreCase(status)) {
            isoRsp.set(39, "00");
        } else {
            isoRsp.set(39, "01");
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(accountEnquiryRsp.getStatus(), 4))
                .append(ISOUtil.zeropad(accountEnquiryRsp.getReason(), 35))
                .append(ISOUtil.zeropad(accountEnquiryRsp.getAccountNumber(), 34))
                .append(ISOUtil.strpad(accountEnquiryRsp.getAccountType(), 35))
                .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorName(), 140))
                .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorId(), 35))
                .append(ISOUtil.strpad(accountEnquiryRsp.getCreditorType(), 35))
                .append(ISOUtil.strpad(accountEnquiryRsp.getResidentStatus(), 35))
                .append(ISOUtil.strpad(accountEnquiryRsp.getTownName(), 35));
        
        return isoRsp;
        
    }

    @Override
    public AccountEnquiryRequest buildRequestMsg(ISOMsg isoMsg) {
        String privateData = isoMsg.getString(62);
        
        AccountEnquiryRequest req = new AccountEnquiryRequest();
        int cursor = 0;
        int endCursor = 8;
        req.setTransactionId(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 8;
        req.setRecipientBank(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 12;
        req.setAmount(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 2;
        req.setCategoryPurpose(privateData.substring(cursor, endCursor));
        
        cursor = cursor + endCursor;
        endCursor = cursor + 35;
        req.setAccountNumber(privateData.substring(cursor, endCursor));
        
        return req;
    }
    
}
