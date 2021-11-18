package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.dto.inbound.CustomerAccountInfoInboundRequest;
import com.mii.komi.dto.inbound.CustomerAccountInfoInboundResponse;
import com.mii.komi.service.ISO8583Service;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Utility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import org.jpos.util.Log;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author vinch
 */
public class InboundCustomerInfoParticipant extends GenericInboundParticipantImpl {
    
    public static String CUSTOMER_INFO_PC = "909200";
    
    Log log = Log.getLog("Q2", this.getClass().getName());

    @Override
    public ISOMsg buildRequestMsg(long id, Serializable context) {
        try {
            Context ctx = (Context) context;
            CustomerAccountInfoInboundRequest customerAccountInfo = (CustomerAccountInfoInboundRequest) ctx.get(Constants.HTTP_REQUEST);
            StringBuilder sb = new StringBuilder();
            String noRef = ISOUtil.strpad(customerAccountInfo.getNoRef(), 20);
            String accountNumber = ISOUtil.strpad(customerAccountInfo.getAccountNumber(), 34);
            sb.append(noRef).append(accountNumber);
            ISOMsg isoMsg = ISO8583Service.buildAdministrativeMsg(CUSTOMER_INFO_PC, customerAccountInfo);
            isoMsg.set(48, sb.toString());
            return isoMsg;
        } catch (ISOException ex) {
            log.error(Utility.getExceptionStackTraceAsString(ex));
            return null;
        }
    }

    @Override
    public ResponseEntity buildSpesificRspBody(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg isoRsp = ctx.get(Constants.ISO_RESPONSE);
        
        CustomerAccountInfoInboundRequest request = (CustomerAccountInfoInboundRequest)ctx.get(Constants.HTTP_REQUEST);
        CustomerAccountInfoInboundResponse rsp = new CustomerAccountInfoInboundResponse();
        rsp.setTransactionId(request.getTransactionId());
        rsp.setDateTime(request.getDateTime());
        rsp.setMerchantType(isoRsp.getString(18));
        rsp.setTerminalId(isoRsp.getString(41));
        String privateData = isoRsp.getString(62);
        String extPrivateData = isoRsp.getString(123);

        int cursor = 0;
        int endCursor = 20;
        rsp.setNoRef(privateData.substring(cursor, endCursor).trim());

        cursor = endCursor;
        endCursor = cursor + 4;
        rsp.setStatus(privateData.substring(cursor, endCursor).trim());

        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setReason(privateData.substring(cursor, endCursor).trim());

        cursor = endCursor;
        endCursor = cursor + 1;
        List<String> emailAddressList = new ArrayList<String>();
        int mailCount = Integer.parseInt(privateData.substring(cursor, endCursor));
        if (mailCount > 0) {
            for (int i = 0; i < mailCount; i++) {
                cursor = endCursor;
                endCursor = cursor + 50;
                emailAddressList.add(privateData.substring(cursor, endCursor).trim());
            }
        }
        rsp.setEmailAddressList(emailAddressList);
        
        cursor = endCursor;
        endCursor = cursor + 1;
        List<String> phoneNumberList = new ArrayList<String>();
        int phoneCount = Integer.parseInt(privateData.substring(cursor, endCursor));
        if (phoneCount > 0) {
            for (int i = 0; i < phoneCount; i++) {
                cursor = endCursor;
                endCursor = cursor + 35;
                phoneNumberList.add(privateData.substring(cursor, endCursor).trim());
            }
        }
        rsp.setPhoneNumberList(phoneNumberList);
        
        cursor = 0;
        endCursor = 34;
        rsp.setAccountNumber(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setAccountType(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 140;
        rsp.setCustomerName(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setCustomerType(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setCustomerId(extPrivateData.substring(cursor, endCursor).trim());

        // adam: added customerIdType
        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setCustomerIdType(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setResidentStatus(extPrivateData.substring(cursor, endCursor).trim());
        
        cursor = endCursor;
        endCursor = cursor + 35;
        rsp.setTownName(extPrivateData.substring(cursor, endCursor).trim());

        return ResponseEntity.ok(rsp);
    }
    
    
    
}
