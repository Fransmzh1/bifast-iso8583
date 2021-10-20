package com.mii.komi.controller;

import com.mii.komi.dto.BaseRequestDTO;
import com.mii.komi.dto.RestResponse;
import com.mii.komi.dto.RootAccountEnquiryRequest;
import com.mii.komi.dto.inbound.CreditTransferInboundRequest;
import com.mii.komi.dto.inbound.CreditTransferInboundResponse;
import com.mii.komi.service.CreditTransferService;
import com.mii.komi.service.ISO8583Service;
import com.mii.komi.service.RESTLoggingService;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Direction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.jpos.util.NameRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/komi-inbound/service")
@Api(tags = {"ISO8583 Adapter API"})
public class BIFastController {

    @Autowired
    private ISO8583Service iso8583Service;

    @Autowired
    private CreditTransferService creditTransferService;

    @Value("${txnmgr.queue}")
    private String txnmgrQueue;
    
    @Value("${txnmgr.timeout}")
    private Long txnmgrTimeout;
    
    @Autowired
    private RESTLoggingService loggingService;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        //ISOMsg rsp = queryTxnMgr(isoMsg);
        /*if ("00".equals(rsp.getBytes(39))) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }*/
        return null;
    }

    @ApiOperation(value = "Account Enquiry", nickname = "Account Enquiry API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/AccountEnquiryRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> accountEnquiry(
            @RequestBody RootAccountEnquiryRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        /*loggingService.log(Direction.INCOMING, 
                request.toString(), 
                httpServletRequest.getMethod(), 
                httpServletRequest.getRemoteAddr(), 
                httpServletRequest.getRequestURI(), 
                "Request");*/
        ResponseEntity<RestResponse> rsp = queryTxnMgr(request.getAccountEnquiryRequest(), "AccountEnquiryRequest");
        /*loggingService.log(Direction.OUTGOING, 
                rsp.getBody().toString(), 
                httpServletRequest.getMethod(), 
                httpServletRequest.getRemoteAddr(), 
                httpServletRequest.getRequestURI(), 
                "Response");*/
        return rsp;
    }

    @ApiOperation(value = "Credit Transfer", nickname = "Core Banking Credit Transfer")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully Post Transfer Data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/CreditTransferRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditTransferInboundResponse> creditTransfer(
            @RequestBody CreditTransferInboundRequest request) throws ISOException, NameRegistrar.NotFoundException {
        return null;
    }
    
    private ResponseEntity<RestResponse> queryTxnMgr(BaseRequestDTO requestDTO, String basepath) {
        String queueKey = requestDTO.getNoRef();
        Context context = new Context();
        context.put(Constants.SELECTOR_KEY, basepath);
        context.put(Constants.HTTP_REQUEST, requestDTO);
        Space space = SpaceFactory.getSpace();
        space.out(txnmgrQueue, context, txnmgrTimeout);
        Context rspContext = (Context) space.in(queueKey, txnmgrTimeout);
        ResponseEntity<RestResponse> rsp = rspContext.get(Constants.HTTP_RESPONSE);
        return rsp;
    }

}
