package com.mii.komi.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.stereotype.Service;

/**
 *
 * @author vinch
 */
@Service
public class ISO8583Service {

    private static final String ISO_MTI_FINANCIAL_REQUEST_1987 = "0200";
    private static final String ISO_MTI_ADMINISTRATIVE_REQUEST_1987 = "0600";
    private static final String ISO_MTI_NETWORK_REQUEST_1987 = "0800";
    private static final String ISO_IDR_CURRENCY_CODE = "360";

    private static int SEQ_NUMBER = 0;

    public static ISOMsg sendMessage(String targetName, ISOMsg isoMsg) throws ISOException {
        try {
            QMUX mux = NameRegistrar.get("mux." + targetName + "-mux");
            if (mux.isConnected()) {
                ISOMsg isoMsgRsp = mux.request(isoMsg, 6000);
                if (isoMsgRsp == null) {
                    isoMsgRsp = (ISOMsg) isoMsg.clone();
                    isoMsgRsp.setResponseMTI();
                    isoMsgRsp.set(39, "91");
                }
                return isoMsgRsp;
            } else {
                ISOMsg isoMsgRsp = (ISOMsg) isoMsg.clone();
                isoMsgRsp = (ISOMsg) isoMsg.clone();
                isoMsgRsp.setResponseMTI();
                isoMsgRsp.set(39, "93");
                return isoMsgRsp;
            }
        } catch (NameRegistrar.NotFoundException ex) {
            ex.printStackTrace();
            ISOMsg isoMsgRsp = (ISOMsg) isoMsg.clone();
            isoMsgRsp = (ISOMsg) isoMsg.clone();
            isoMsgRsp.setResponseMTI();
            isoMsgRsp.set(39, "91");
            return isoMsgRsp;
        }
    }

    public static ISOMsg buildFinancialMsg(String processingCode, String uniqueReff, String terminalType) throws ISOException {
        LocalDateTime currentGMT = LocalDateTime.now(ZoneId.of("GMT"));
        LocalDateTime current = LocalDateTime.now();
        String currentStringGMTDate = currentGMT.format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        String currentDate = current.format(DateTimeFormatter.ofPattern("MMdd"));
        String currentTime = current.format(DateTimeFormatter.ofPattern("HHmmss"));
        String stan = ISOUtil.zeropad(1, 6);
        //String rrn = ISOUtil.zeropad(uniqueReff, 12);

        ISOMsg isomsg = new ISOMsg();
        isomsg.setMTI(ISO_MTI_FINANCIAL_REQUEST_1987);
        isomsg.set(3, processingCode);
        isomsg.set(7, currentStringGMTDate);
        isomsg.set(11, stan);
        //isomsg.set(12, currentTime);
        isomsg.set(18, "6018");
        //isomsg.set(13, currentDate);
        isomsg.set(32, "000");
        //isomsg.set(37, rrn);
        isomsg.set(49, ISO_IDR_CURRENCY_CODE);
        return isomsg;
    }

    public static ISOMsg buildNetworkMsg(String networkMgmtType) throws ISOException {
        LocalDateTime currentGMT = LocalDateTime.now(ZoneId.of("GMT"));
        String stan = ISOUtil.zeropad("000001", 6);
        String currentDateTime = currentGMT.format(DateTimeFormatter.ofPattern("MMddHHmmss"));

        ISOMsg isomsg = new ISOMsg();
        isomsg.setMTI(ISO_MTI_NETWORK_REQUEST_1987);
        isomsg.set(7, currentDateTime);
        isomsg.set(11, stan);
        isomsg.set(70, networkMgmtType);
        return isomsg;
    }

}
