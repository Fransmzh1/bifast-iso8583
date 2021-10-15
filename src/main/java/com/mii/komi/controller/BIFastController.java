package com.mii.komi.controller;

import com.mii.komi.dto.AccountEnquiryRequest;
import com.mii.komi.dto.AccountEnquiryResponse;
import com.mii.komi.dto.inbound.CreditTransferInboundRequest;
import com.mii.komi.dto.inbound.CreditTransferInboundResponse;
import com.mii.komi.service.AccountEnquiryInboundService;
import com.mii.komi.service.CreditTransferService;
import com.mii.komi.service.ISO8583Service;
import com.mii.komi.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.jpos.util.NameRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vinch
 */
@RestController
@RequestMapping("/iso8583")
@Api(tags = {"ISO8583 Adapter API"})
public class BIFastController {

    @Autowired
    private ISO8583Service iso8583Service;

    @Autowired
    private AccountEnquiryInboundService accountEnquiryService;

    @Autowired
    private CreditTransferService creditTransferService;

    @Value("${txnmgr.queue}")
    private String txnmgrQueue;
    
    @Value("${txnmgr.timeout}")
    private Long txnmgrTimeout;

    @ApiOperation(value = "Network Management", nickname = "ISO8583 Network Management API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/netman", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity echoTest() throws ISOException, NameRegistrar.NotFoundException {
        ISOMsg isoMsg = iso8583Service.buildNetworkMsg("001");
        ISOMsg rsp = queryTxnMgr(isoMsg);
        if ("00".equals(rsp.getBytes(39))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiOperation(value = "Account Enquiry", nickname = "Account Enquiry API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/account/enquiry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountEnquiryResponse> accountEnquiry(@RequestBody AccountEnquiryRequest request) throws ISOException, NameRegistrar.NotFoundException {
        ISOMsg isoMsg = accountEnquiryService.buildRequestMsg(request);
        ISOMsg rsp = queryTxnMgr(isoMsg);
        AccountEnquiryResponse httpRsp = (AccountEnquiryResponse) accountEnquiryService.buildResponseMsg(rsp);
        return ResponseEntity.ok().body(httpRsp);
    }

    @ApiOperation(value = "Credit Transfer", nickname = "Core Banking Credit Transfer")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully Post Transfer Data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/account/credit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditTransferInboundResponse> creditTransfer(
            @RequestBody CreditTransferInboundRequest request) throws ISOException, NameRegistrar.NotFoundException {
        ISOMsg isoMsg = creditTransferService.buildRequestMsg(request);
        ISOMsg rsp = iso8583Service.sendMessage("corebanking", isoMsg);
        CreditTransferInboundResponse httpRsp = (CreditTransferInboundResponse) creditTransferService.buildResponseMsg(rsp);
        return ResponseEntity.ok().body(httpRsp);
    }
    
    private ISOMsg queryTxnMgr(ISOMsg isoMsg) {
        String queueKey = isoMsg.getString(7) + isoMsg.getString(11);
        Context context = new Context();
        context.put(Constants.REQUEST_KEY, isoMsg);
        Space space = SpaceFactory.getSpace();
        space.out(txnmgrQueue, context, txnmgrTimeout);
        Context rspContext = (Context) space.in(queueKey, txnmgrTimeout);

        ISOMsg rsp = rspContext.get(Constants.RESPONSE_KEY);
        return rsp;
    }

}
