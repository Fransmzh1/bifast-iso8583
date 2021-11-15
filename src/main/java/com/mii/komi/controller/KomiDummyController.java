package com.mii.komi.controller;

import com.mii.komi.dto.outbound.*;
import com.mii.komi.dto.outbound.requestroot.RootAccountEnquiry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.jpos.iso.ISOException;
import org.jpos.util.NameRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "", allowedHeaders = "", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.OPTIONS,
        RequestMethod.PUT,
        RequestMethod.PATCH,
        RequestMethod.HEAD,
        RequestMethod.DELETE,
        RequestMethod.TRACE})
@RestController
@RequestMapping("/komidummy/api")
@Api(tags = {"ISO8583 Adapter API"})
public class KomiDummyController {

    @Value("${rjct.refNum}")
    private String rjctRefNum;

    @ApiOperation(value = "Account Enquiry", nickname = "Account Enquiry API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get data"),
            @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Page Not Found")
    })
    @RequestMapping(path = "/accountinquiry", produces = MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
    public ResponseEntity<RestResponse> accountEnquiry(
            @RequestBody RootAccountEnquiry request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
//        ResponseEntity rsp = queryTxnMgr(request, "AccountEnquiry");
        System.out.println("fajar " + rjctRefNum +" " +request.getAccountEnquiryRequest().getNoRef());
        if(request!=null && !request.getAccountEnquiryRequest().getNoRef().equalsIgnoreCase(rjctRefNum)){
            List<AccountEnquiryOutboundResponse> list = new ArrayList<>();
            AccountEnquiryOutboundResponse rspDummy = new AccountEnquiryOutboundResponse();
            rspDummy.setAccountNumber(request.getAccountEnquiryRequest().getRecipientAccountNumber());
            rspDummy.setAccountType("CACC");
            rspDummy.setCreditorId("KTP-208472701");
            rspDummy.setCreditorName("Adiputro Erwin");
            rspDummy.setCreditorType("01");
            rspDummy.setResidentStatus("01");
            rspDummy.setTownName("0300");
            rspDummy.setNoRef(request.getAccountEnquiryRequest().getNoRef());
            rspDummy.setProxyId(request.getAccountEnquiryRequest().getProxyId());
            rspDummy.setProxyType(request.getAccountEnquiryRequest().getProxyType());

            list.add(rspDummy);
            return ResponseEntity.ok(RestResponse.success("Success/ Transaction Accepted",list));
        }else{
            List<AccountEnquiryOutboundRequestReject> list = new ArrayList<>();
            AccountEnquiryOutboundRequestReject rspReject = new AccountEnquiryOutboundRequestReject();
            rspReject.setAccountNumber(request.getAccountEnquiryRequest().getSenderAccountNumber());
            rspReject.setNoRef(request.getAccountEnquiryRequest().getNoRef());
            list.add(rspReject);
            return ResponseEntity.ok(RestResponse.failed("U149","DuplicateTransaction","RJCT",list));
        }
    }

}
