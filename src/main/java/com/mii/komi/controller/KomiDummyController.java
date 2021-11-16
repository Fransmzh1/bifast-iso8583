package com.mii.komi.controller;

import com.mii.komi.dto.outbound.*;
import com.mii.komi.dto.outbound.requestroot.*;
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
//        System.out.println("fajar " + rjctRefNum +" " +request.getAccountEnquiryRequest().getNoRef());
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

    @ApiOperation(value = "Payment Status", nickname = "Payment Status API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get data"),
            @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Page Not Found")
    })
    @RequestMapping(path = "/paymentstatus", produces = MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
    public ResponseEntity<RestResponse> paymentStatus(
            @RequestBody RootPaymentStatus request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
//        ResponseEntity rsp = queryTxnMgr(request, "AccountEnquiry");
//        System.out.println("fajar " + rjctRefNum +" " +request.getAccountEnquiryRequest().getNoRef());
        if(request!=null && !request.getPaymentStatusRequest().getNoRef().equalsIgnoreCase(rjctRefNum)){
            List<PaymentStatusResponse> list = new ArrayList<>();
            PaymentStatusResponse rspDummy = new PaymentStatusResponse();
            rspDummy.setNoRef(request.getPaymentStatusRequest().getNoRef());
            rspDummy.setTerminalId("333-566652");
            rspDummy.setCategoryPurpose("01");
            rspDummy.setDebtorName("Joko Susilo");
            rspDummy.setDebtorId("11264145");
            rspDummy.setDebtorType("01");
            rspDummy.setDebtorAccountNumber("7744404");
            rspDummy.setDebtorAccountType("CACC");
            rspDummy.setDebtorResidentialStatus("01");
            rspDummy.setDebtorTownName("0300");
            rspDummy.setAmount("438417.00");
            rspDummy.setFeeTransfer("6500.00");
            rspDummy.setRecipientBank("BSMDIDJA");
            rspDummy.setCreditorType("01");
            rspDummy.setCreditorId("C224403");
            rspDummy.setCreditorAccountNumber("174551");
            rspDummy.setCreditorAccountType("SVGS");
            rspDummy.setCreditorProxyId("bambang@gmail.com");
            rspDummy.setCreditorProxyType("02");
            rspDummy.setPaymentInformation("untuk pembayaran pembelian");

            list.add(rspDummy);
            return ResponseEntity.ok(RestResponse.success("Success/ Transaction Accepted",list));
        }else{
            return ResponseEntity.ok(RestResponse.failed("U149","DuplicateTransaction","RJCT"));
        }
    }

    @ApiOperation(value = "Credit Transfer", nickname = "Credit Transfer API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get data"),
            @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Page Not Found")
    })
    @RequestMapping(path = "/credittransfer", produces = MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
    public ResponseEntity<RestResponse> creditTransfer(
            @RequestBody RootCreditTransfer request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
//        ResponseEntity rsp = queryTxnMgr(request, "AccountEnquiry");
//        System.out.println("fajar " + rjctRefNum +" " +request.getAccountEnquiryRequest().getNoRef());
        if(request!=null && !request.getCreditTransferRequest().getNoRef().equalsIgnoreCase(rjctRefNum)){
            List<CreditTransferOutboundResponse> list = new ArrayList<>();
            CreditTransferOutboundResponse rspDummy = new CreditTransferOutboundResponse();
            rspDummy.setNoRef(request.getCreditTransferRequest().getNoRef());
            rspDummy.setAccountNumber(request.getCreditTransferRequest().getCreditorAccountNumber());
            rspDummy.setCreditorName(request.getCreditTransferRequest().getCreditorName());

            list.add(rspDummy);
            return ResponseEntity.ok(RestResponse.success("Success/ Transaction Accepted",list));
        }else{
            return ResponseEntity.ok(RestResponse.failed("U149","DuplicateTransaction","RJCT"));
        }
    }

    @ApiOperation(value = "Proxy Registration Request", nickname = "Proxy Registration Request API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get data"),
            @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Page Not Found")
    })
    @RequestMapping(path = "/proxyregistrationrequest", produces = MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
    public ResponseEntity<RestResponse> proxyRegistrationRequest(
            @RequestBody RootProxyRegistration request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
//        ResponseEntity rsp = queryTxnMgr(request, "AccountEnquiry");
//        System.out.println("fajar " + rjctRefNum +" " +request.getAccountEnquiryRequest().getNoRef());
        if(request!=null && !request.getProxyRegistrationRequest().getNoRef().equalsIgnoreCase(rjctRefNum)){
            List<ProxyRegistrationResponse> list = new ArrayList<>();
            ProxyRegistrationResponse rspDummy = new ProxyRegistrationResponse();
            rspDummy.setNoRef(request.getProxyRegistrationRequest().getNoRef());
            rspDummy.setRegistrationId("000121");
            rspDummy.setRegistrationType(request.getProxyRegistrationRequest().getRegistrationType());

            list.add(rspDummy);
            return ResponseEntity.ok(RestResponse.success("Success/ Transaction Accepted",list));
        }else{
            return ResponseEntity.ok(RestResponse.failed("U149","DuplicateTransaction","RJCT"));
        }
    }

    @ApiOperation(value = "Proxy Registration Request", nickname = "Proxy Registration Request API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get data"),
            @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Page Not Found")
    })
    @RequestMapping(path = "/proxyresolutionrequest", produces = MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
    public ResponseEntity<RestResponse> proxyResolutionRequest(
            @RequestBody RootProxyResolution request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
//        ResponseEntity rsp = queryTxnMgr(request, "AccountEnquiry");
//        System.out.println("fajar " + rjctRefNum +" " +request.getAccountEnquiryRequest().getNoRef());
        if(request!=null && !request.getProxyResolutionRequest().getNoRef().equalsIgnoreCase(rjctRefNum)){
            List<ProxyResolutionResponse> list = new ArrayList<>();
            ProxyResolutionResponse rspDummy = new ProxyResolutionResponse();
            rspDummy.setNoRef(request.getProxyResolutionRequest().getNoRef());
            rspDummy.setProxyType(request.getProxyResolutionRequest().getProxyType());
            rspDummy.setProxyValue(request.getProxyResolutionRequest().getProxyValue());
            rspDummy.setRegistrationId("12341234");
            rspDummy.setDisplayName("JOHN SMITH");
            rspDummy.setRegisterBank("SIHBIDJ1");
            rspDummy.setAccountNumber("03005000069295");
            rspDummy.setAccountType("CACC");
            rspDummy.setAccountName("JOHN SMITH");
            rspDummy.setCustomerType("01");
            rspDummy.setCustomerId("KTP-208472701");
            rspDummy.setResidentialStatus("01");
            rspDummy.setTownName("0300");

            list.add(rspDummy);
            return ResponseEntity.ok(RestResponse.success("Success/ Transaction Accepted",list));
        }else{
            return ResponseEntity.ok(RestResponse.failed("U149","DuplicateTransaction","RJCT"));
        }
    }
}
