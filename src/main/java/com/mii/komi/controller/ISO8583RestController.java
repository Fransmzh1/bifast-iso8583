package com.mii.komi.controller;

import com.mii.komi.service.ISO8583Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.json.JSONArray;
import org.json.JSONObject;
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
@RequestMapping("/iso8583/service")
@Api(tags = {"ISO8583 Services"})
public class ISO8583RestController {

    @ApiOperation(value = "ISO8583 Adapter", nickname = "ISO Sender")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @PostMapping(path = "/sendAndReceive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendAndReceiveISO8583(
            @RequestBody String request,
            HttpServletRequest httpServletRequest) throws ISOException, NameRegistrar.NotFoundException {
        JSONObject reqObj = new JSONObject(request);
        JSONArray reqArray = reqObj.getJSONArray("isoFields");
        ISOMsg isoMsg = new ISOMsg();
        //isoMsg.setDirection(ISOMsg.OUTGOING);
        for (int i = 0; i < reqArray.length(); i++) {
            JSONObject fieldObject = reqArray.getJSONObject(i);
            int isoField = fieldObject.getInt("number");
            String isoValue = fieldObject.getString("value");
            isoMsg.set(isoField, isoValue);
        }
        ISOMsg rspMsg;
        JSONArray arrayRsp = new JSONArray();
        rspMsg = ISO8583Service.sendMessage("as400", isoMsg);
        if (rspMsg == null) {
            rspMsg = (ISOMsg) isoMsg.clone();
            rspMsg.setResponseMTI();
            rspMsg.set(39, "68");
        }
        JSONObject jsonMTIField = new JSONObject();
        jsonMTIField.put("number", 0);
        jsonMTIField.put("value", rspMsg.getMTI());
        arrayRsp.put(0, jsonMTIField);

        for (int i = 2; i <= rspMsg.getMaxField(); i++) {
            if (rspMsg.hasField(i)) {
                JSONObject jsonField = new JSONObject();
                jsonField.put("number", i);
                jsonField.put("value", rspMsg.getString(i));
                arrayRsp.put(jsonField);
            }
        }
        JSONObject rsp = new JSONObject();
        rsp.put("isoFields", arrayRsp);
        return ResponseEntity.ok(rsp.toString(5));
    }

}
