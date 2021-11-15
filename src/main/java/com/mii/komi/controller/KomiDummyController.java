package com.mii.komi.controller;

import com.mii.komi.dto.outbound.AccountEnquiryOutboundRequest;
import com.mii.komi.dto.outbound.AccountEnquiryOutboundResponse;
import com.mii.komi.dto.outbound.AccountEnquiryOutboundResponseDummy;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.dto.outbound.requestroot.RootAccountEnquiry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.jpos.iso.ISOException;
import org.jpos.util.NameRegistrar;
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
//        System.out.println("fajar " + request.getAccountEnquiryRequest().toString() +" " +request.getAccountEnquiryRequest().getNoRef());
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

        String rjct ="{\n" +
                "   \"ResponseCode\": \"RJCT\",\n" +
                "   \"ReasonCode\": \"U149\",\n" +
                "   \"ReasonMessage \": \"DuplicateTransaction\"\n" +
                "   \"Date\": \"20211002\",\n" +
                "   \"Time\": \"153500\",\n" +
                "   \"Content\": [{\n" +
                "\"NoRef\": \"202010204556773\",\n" +
                "\"AccountNumber\": \"677589\"\n" +
                "}] }";

       

        return ResponseEntity.ok(RestResponse.success("Success/ Transaction Accepted",list));
    }

}
