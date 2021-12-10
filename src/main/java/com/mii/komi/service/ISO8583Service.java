package com.mii.komi.service;

import com.mii.komi.dto.inbound.BaseInboundRequestDTO;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.iso.ChannelAdaptor;
import org.jpos.q2.iso.QMUX;
import org.jpos.q2.iso.QServer;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
@Service
public class ISO8583Service {

    private static final String ISO_MTI_FINANCIAL_REQUEST_1987 = "0200";
    private static final String ISO_MTI_ADMINISTRATIVE_REQUEST_1987 = "0600";
    private static final String ISO_MTI_NETWORK_REQUEST_1987 = "0800";
    private static final String ISO_IDR_CURRENCY_CODE = "360";
    private static final String ISO_LOCAL_BANK_CODE = "564";

    private static int SEQ_NUMBER = 0;

    public static boolean isConnected(String targetName) {
        boolean connected = false;
        try {
            Object o = NameRegistrar.get(targetName);
            if (o instanceof QServer) {
                ISOServer isoServer = NameRegistrar.get("server." + targetName);
                if (isoServer.getActiveConnections() > 0) {
                    connected = true;
                }
            } else if (o instanceof ChannelAdaptor) {
                QMUX mux = NameRegistrar.get("mux." + targetName + "-mux");
                if (mux.isConnected()) {
                    connected = true;
                }
            }
            return connected;
        } catch (NameRegistrar.NotFoundException ex) {
            return false;
        }
    }
    
    public static int[] getInts (String name, String delimeter) {
        String[] ss = name.split(delimeter);
        int[] ii = new int[ss.length];
        for (int i=0; i<ss.length; i++)
            ii[i] = Integer.parseInt(ss[i].trim());
        return ii;
    }
    
    public static boolean validateMandatoryFields(ISOMsg isoMsg, int[] mandatoryFields) {
        for(int m : mandatoryFields) {
            if(!isoMsg.hasField(m)) {
                return false;
            }
        }
        return true;
    }

    public static ISOMsg buildFinancialMsg(String processingCode, BaseInboundRequestDTO requestDTO) throws ISOException {
        LocalDateTime currentGMT = LocalDateTime.now(ZoneId.of("GMT"));
        LocalDateTime current = LocalDateTime.now();
        String currentStringGMTDate = currentGMT.format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        String currentLocalDate = current.format(DateTimeFormatter.ofPattern("MMdd"));
        String currentLocalTime = current.format(DateTimeFormatter.ofPattern("HHmmss"));

        ISOMsg isomsg = new ISOMsg();
        isomsg.setMTI(ISO_MTI_FINANCIAL_REQUEST_1987);
        isomsg.set(3, processingCode);
        isomsg.set(7, currentStringGMTDate);
        isomsg.set(11, requestDTO.getTransactionId());
        //isomsg.set(12, currentLocalTime);
        //isomsg.set(13, currentLocalDate);
        isomsg.set(18, requestDTO.getMerchantType());
        //isomsg.set(32, ISO_LOCAL_BANK_CODE);
        //isomsg.set(37, ISOUtil.zeropad(requestDTO.getTransactionId(), 12));
        isomsg.set(41, requestDTO.getTerminalId());
        //isomsg.set(49, ISO_IDR_CURRENCY_CODE);
        isomsg.set(63, requestDTO.getNoRef());
        return isomsg;
    }

    public static ISOMsg buildAdministrativeMsg(String processingCode, BaseInboundRequestDTO requestDTO) throws ISOException {
        LocalDateTime currentGMT = LocalDateTime.now(ZoneId.of("GMT"));
        LocalDateTime current = LocalDateTime.now();
        String currentStringGMTDate = currentGMT.format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        String currentLocalDate = current.format(DateTimeFormatter.ofPattern("MMdd"));
        String currentLocalTime = current.format(DateTimeFormatter.ofPattern("HHmmss"));

        ISOMsg isomsg = new ISOMsg();
        isomsg.setMTI(ISO_MTI_ADMINISTRATIVE_REQUEST_1987);
        isomsg.set(3, processingCode);
        isomsg.set(7, currentStringGMTDate);
        isomsg.set(11, requestDTO.getTransactionId());
        //isomsg.set(12, currentLocalTime);
        //isomsg.set(13, currentLocalDate);
        isomsg.set(18, requestDTO.getMerchantType());
        //isomsg.set(32, ISO_LOCAL_BANK_CODE);
        //isomsg.set(37, ISOUtil.zeropad(requestDTO.getTransactionId(), 12));
        isomsg.set(41, requestDTO.getTerminalId());
        //isomsg.set(49, ISO_IDR_CURRENCY_CODE);
        isomsg.set(63, requestDTO.getNoRef());
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
