package com.mii.komi.controller;

import com.mii.komi.dto.inbound.AccountEnquiryInboundRequest;
import com.mii.komi.dto.inbound.BalanceInquiryRequest;
import com.mii.komi.dto.inbound.BaseInboundRequestDTO;
import com.mii.komi.dto.inbound.CreditTransferInboundRequest;
import com.mii.komi.dto.inbound.CustomerAccountInfoInboundRequest;
import com.mii.komi.dto.inbound.DebitReversalInboundRequest;
import com.mii.komi.dto.inbound.DebitTransferInboundRequest;
import com.mii.komi.dto.inbound.SettlementRequest;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Utility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import org.jpos.iso.ISOException;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.jpos.util.NameRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
@RestController
@RequestMapping("/komi/api/v1/adapter")
@Api(tags = {"ISO8583 Adapter API"})
public class BIFastController {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${txnmgr.queue}")
    private String txnmgrQueue;
    
    @Value("${txnmgr.timeout}")
    private Long txnmgrTimeout;

    @ApiOperation(value = "Account Enquiry", nickname = "Account Enquiry API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/accountinquiry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity accountEnquiry(
            @RequestBody AccountEnquiryInboundRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        ResponseEntity rsp = queryTxnMgr(request, "AccountEnquiry");
        return rsp;
    }
    
    @ApiOperation(value = "Debit Transfer Request", nickname = "Debit Transfer API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully POST data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/debit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity debit(
            @RequestBody DebitTransferInboundRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        ResponseEntity rsp = queryTxnMgr(request, "DebitTransfer");
        return rsp;
    }
    
    @ApiOperation(value = "Credit Transfer Request", nickname = "Credit Transfer API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully POST data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/credit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity credit(
            @RequestBody CreditTransferInboundRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        ResponseEntity rsp = queryTxnMgr(request, "CreditTransfer");
        return rsp;
    }

/*
    @ApiOperation(value = "Credit Reversal Request", nickname = "Credit Reversal API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully POST data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/creditreversal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity creditreversal(
            @RequestBody CreditReversalRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        ResponseEntity rsp = queryTxnMgr(request, "CreditReversal");
        return rsp;
    }
*/    

    @ApiOperation(value = "Debit Reversal Request", nickname = "Debit Reversal API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully POST data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/debitreversal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity debitReversal(
            @RequestBody DebitReversalInboundRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        ResponseEntity rsp = queryTxnMgr(request, "DebitReversal");
        return rsp;
    }
    
    @ApiOperation(value = "Settlement Request", nickname = "Settlement API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully POST data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/settlement", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity settlement(
            @RequestBody SettlementRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        ResponseEntity rsp = queryTxnMgr(request, "Settlement");
        return rsp;
    }
    
    @ApiOperation(value = "Balance Inquiry Request", nickname = "Balance Inquiry API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully POST data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/balanceinquiry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity balanceInquiry(
            @RequestBody BalanceInquiryRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        ResponseEntity rsp = queryTxnMgr(request, "BalanceInquiry");
        return rsp;
    }
    
    @ApiOperation(value = "Balance Inquiry Request", nickname = "Balance Inquiry API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully POST data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/emailphonelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity accountCustomerInfo(
            @RequestBody CustomerAccountInfoInboundRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        ResponseEntity rsp = queryTxnMgr(request, "CustomerInfo");
        return rsp;
    }
    
    private ResponseEntity queryTxnMgr(BaseInboundRequestDTO baseRequestDTO, String basepath) {
        String queueKey = Utility.generateUUID();
        Context context = new Context();
        context.put(Constants.SELECTOR_KEY, basepath);
        context.put(Constants.HTTP_REQUEST, baseRequestDTO);
        context.put(Constants.QUEUE_KEY, queueKey);
        Space space = SpaceFactory.getSpace();
        space.out(txnmgrQueue, context, txnmgrTimeout);
        Context rspContext = (Context) space.in(queueKey, txnmgrTimeout);
        ResponseEntity<RestResponse> rsp = rspContext.get(Constants.HTTP_RESPONSE);
        return rsp;
    }

}
