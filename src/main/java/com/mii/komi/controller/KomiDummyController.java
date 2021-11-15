package com.mii.komi.controller;

import com.mii.komi.dto.outbound.AccountEnquiryOutboundRequest;
import com.mii.komi.dto.outbound.AccountEnquiryOutboundResponseDummy;
import com.mii.komi.dto.outbound.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.jpos.iso.ISOException;
import org.jpos.util.NameRegistrar;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    @PostMapping(path = "/accountinquiry", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<RestResponse> accountEnquiry(
            @RequestBody AccountEnquiryOutboundRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
//        ResponseEntity rsp = queryTxnMgr(request, "AccountEnquiry");
        List<AccountEnquiryOutboundResponseDummy> list = new ArrayList<>();
        AccountEnquiryOutboundResponseDummy rspDummy = new AccountEnquiryOutboundResponseDummy();
        rspDummy.setAccountNumber("677589");
        rspDummy.setAccountType("CACC");
        rspDummy.setCreditorId("KTP-208472701");
        rspDummy.setCreditorName("Adiputro Erwin");
        rspDummy.setNoRef("000000556773");
        rspDummy.setCreditorType("01");
        rspDummy.setResidentStatus("01");
        rspDummy.setTownName("0300");
        rspDummy.setNoRef("000000556773");

        list.add(rspDummy);
        String rsp ="{\n" +
                "   \"ResponseCode\": \"ACTC\",\n" +
                "   \"ReasonCode\": \"U000\",\n" +
                "   \"ReasonMessage\": \"Success/ Transaction Accepted\",\n" +
                "   \"Date\": \"20211002\",\n" +
                "   \"Time\": \"153500\",\n" +
                "   \"Content\": [{\n" +
                "\"NoRef\": \"000000556773\",\n" +
                "\"AccountNumber\": \"677589\",\n" +
                "\"AccountType\": \"CACC\",\n" +
                "\"CreditorId\": \"KTP-208472701\",\n" +
                "\"CreditorName\": \"Adiputro Erwin\",\n" +
                "\"CreditorType\": \"01\",\n" +
                "\"ResidentStatus\": \"01\",\n" +
                "\"TownName\": \"0300\"\n" +
                "}] }";

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

        String pnding = "{\n" +
                "   \"ResponseCode\": \"KSTS\",\n" +
                "   \"ReasonCode\": \"U000\",\n" +
                "   \"ReasonMessage \": \"Internal Timeout\",\n" +
                "   \"Date\": \"20211002\",\n" +
                "   \"Time\": \"153500\",\n" +
                "   \"Content\": [{\n" +
                "\"NoRef\": \"202010204556773\",\n" +
                "\"AccountNumber\": \"677589\"\n" +
                "}] }";

        return RestResponse.success("Success/ Transaction Accepted",list);
    }

}
