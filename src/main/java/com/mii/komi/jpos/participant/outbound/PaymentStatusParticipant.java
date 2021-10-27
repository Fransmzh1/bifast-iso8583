package com.mii.komi.jpos.participant.outbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mii.komi.dto.outbound.BaseOutboundDTO;
import com.mii.komi.dto.outbound.PaymentStatusRequest;
import com.mii.komi.dto.outbound.PaymentStatusResponse;
import com.mii.komi.dto.outbound.RestResponse;
import com.mii.komi.exception.DataNotFoundException;
import com.mii.komi.exception.HttpRequestException;
import com.mii.komi.exception.RestTemplateResponseErrorHandler;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Utility;
import java.io.Serializable;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.transaction.Context;
import static org.jpos.transaction.TransactionConstants.ABORTED;
import static org.jpos.transaction.TransactionConstants.PREPARED;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vinch
 */
public class PaymentStatusParticipant extends OutboundParticipant {

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            PaymentStatusRequest paymentStatusRequest = (PaymentStatusRequest) buildRequestMsg(reqMsg);
            ctx.put(Constants.HTTP_REQUEST, paymentStatusRequest);
            String endpointKomi = cfg.get("endpoint");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
            RestResponse<PaymentStatusResponse> accountEnquiryResponse = restTemplate.postForObject(endpointKomi,
                    paymentStatusRequest,
                    RestResponse.class);
            ctx.put(Constants.HTTP_RESPONSE, accountEnquiryResponse);
            return PREPARED;
        } catch (DataNotFoundException ex) {
            ex.printStackTrace();
            return ABORTED;
        } catch (HttpRequestException ex) {
            if (ex.getMessage() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                PaymentStatusResponse paymentStatusResponse;
                try {
                    paymentStatusResponse = objectMapper.readValue(ex.getMessage(), PaymentStatusResponse.class);
                    ctx.put(Constants.HTTP_RESPONSE, paymentStatusResponse);
                } catch (JsonProcessingException ex1) {
                    ex1.printStackTrace();
                }
            }
            return ABORTED;
        } catch (ISOException ex) {
            ex.printStackTrace();
            return ABORTED;
        }
    }

    @Override
    public Object buildRequestMsg(ISOMsg isoMsg) throws ISOException {
        String privateData = isoMsg.getString(48);

        PaymentStatusRequest req = new PaymentStatusRequest();
        int cursor = 0;
        int endCursor = 20;
        req.setNoRef(privateData.substring(cursor, endCursor));

        cursor = endCursor;
        endCursor = cursor + 20;
        req.setOriginalNoRef(privateData.substring(cursor, endCursor));

        return req;
    }

    @Override
    public ISOMsg buildFailedResponseMsg(ISOMsg req, RestResponse<BaseOutboundDTO> rr) {
        try {
            String privateData = req.getString(48);
            String originalNoRef = privateData.substring(20, 40);

            PaymentStatusResponse paymentStatusResponse = (PaymentStatusResponse) rr.getContent().get(0);
            ISOMsg isoRsp = (ISOMsg) req.clone();
            isoRsp.setResponseMTI();
            isoRsp.set(39, "81");
            StringBuilder sb = new StringBuilder();
            sb.append(ISOUtil.strpad(paymentStatusResponse.getNoRef(), 20))
                    .append(ISOUtil.strpad(rr.getResponseCode(), 4))
                    .append(ISOUtil.strpad(rr.getReasonCode(), 35))
                    .append(originalNoRef)
                    .append(ISOUtil.strpad(Utility.getOriginalDateTimeFromOriginalNoRef(originalNoRef), 10))
                    .append(ISOUtil.zeropad(paymentStatusResponse.getCategoryPurpose(), 2))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorName(), 140))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorType(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorId(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorAccountNumber(), 34))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorAccountType(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorResidentialStatus(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorTownName(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getAmount(), 18))
                    .append(ISOUtil.strpad(paymentStatusResponse.getFeeTransfer(), 18));
            isoRsp.set(62, sb.toString());
            return isoRsp;
        } catch (ISOException ex) {
            return null;
        }
    }

    @Override
    public ISOMsg buildResponseMsg(ISOMsg req, RestResponse<BaseOutboundDTO> dto) throws ISOException {
        PaymentStatusResponse paymentStatusResponse = (PaymentStatusResponse) dto.getContent().get(0);
        String privateData = req.getString(48);
        String originalNoRef = privateData.substring(20, 40);

        ISOMsg isoRsp = (ISOMsg) req.clone();
        isoRsp.setResponseMTI();
        isoRsp.set(39, "81");
        StringBuilder sb = new StringBuilder();
        sb.append(ISOUtil.strpad(paymentStatusResponse.getNoRef(), 20))
                    .append(ISOUtil.strpad(dto.getResponseCode(), 4))
                    .append(ISOUtil.strpad(dto.getReasonCode(), 35))
                    .append(originalNoRef)
                    .append(ISOUtil.strpad(Utility.getOriginalDateTimeFromOriginalNoRef(originalNoRef), 10))
                    .append(ISOUtil.zeropad(paymentStatusResponse.getCategoryPurpose(), 2))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorName(), 140))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorType(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorId(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorAccountNumber(), 34))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorAccountType(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorResidentialStatus(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getDebtorTownName(), 35))
                    .append(ISOUtil.strpad(paymentStatusResponse.getAmount(), 18))
                    .append(ISOUtil.strpad(paymentStatusResponse.getFeeTransfer(), 18));
        isoRsp.set(62, sb.toString());
        return isoRsp;
    }

}
