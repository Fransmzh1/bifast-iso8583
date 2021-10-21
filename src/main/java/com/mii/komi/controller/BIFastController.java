package com.mii.komi.controller;

import com.mii.komi.dto.BaseRootHttpRequest;
import com.mii.komi.dto.RestResponse;
import com.mii.komi.dto.RootAccountEnquiryRequest;
import com.mii.komi.service.RESTLoggingService;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Direction;
import com.mii.komi.util.Utility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.HttpMethod;
import javax.servlet.http.HttpServletRequest;
import org.jpos.iso.ISOException;
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
 * @author Erwin Sugianto Santoso - MII
 */
@RestController
@RequestMapping("/komi-inbound/service")
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
    @PostMapping(path = "/AccountEnquiryRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> accountEnquiry(
            @RequestBody RootAccountEnquiryRequest request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        ResponseEntity<RestResponse> rsp = queryTxnMgr(request, "AccountEnquiryRequest");
        return rsp;
    }
    
    private ResponseEntity<RestResponse> queryTxnMgr(BaseRootHttpRequest baseRootRequest, String basepath) {
        String queueKey = Utility.generateUUID();
        Context context = new Context();
        context.put(Constants.SELECTOR_KEY, basepath);
        context.put(Constants.HTTP_REQUEST, baseRootRequest);
        context.put(Constants.QUEUE_KEY, queueKey);
        Space space = SpaceFactory.getSpace();
        space.out(txnmgrQueue, context, txnmgrTimeout);
        Context rspContext = (Context) space.in(queueKey, txnmgrTimeout);
        ResponseEntity<RestResponse> rsp = rspContext.get(Constants.HTTP_RESPONSE);
        return rsp;
    }

}
