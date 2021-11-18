package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.service.ISO8583Service;
import com.mii.komi.util.Constants;
import com.mii.komi.util.Utility;
import java.io.Serializable;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.jpos.transaction.Context;
import static org.jpos.transaction.TransactionConstants.NO_JOIN;
import static org.jpos.transaction.TransactionConstants.PREPARED;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;
import org.jpos.util.NameRegistrar;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class SendAndReceiveIso8583 implements TransactionParticipant, Configurable {

    private Configuration cfg;
    private String destination;
    private String mandatoryFields;
    private long timeout;

    Log log = Log.getLog("Q2", this.getClass().getName());

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.ISO_REQUEST);
        try {
            QMUX mux = NameRegistrar.get("mux." + destination + "-mux");
            if (ISO8583Service.isConnected(destination)) {
                ISOMsg isoMsgRsp = mux.request(reqMsg, timeout);
                if (isoMsgRsp == null) {
                    log.info("No response from : '" + destination + "'");
                    ctx.put(Constants.STATUS, Constants.STATUS_NO_RESPONSE);
                    return ABORTED | NO_JOIN;
                } else {
                    ctx.put(Constants.ISO_RESPONSE, isoMsgRsp);
                    if (!isoMsgRsp.hasFields(ISO8583Service.getInts(mandatoryFields, ","))) {
                        log.info("ISO Response from '"+destination+"' are missing some mandatory fields");
                        ctx.put(Constants.STATUS, Constants.STATUS_BAD_RESPONSE);
                        return ABORTED | NO_JOIN;
                    }
                    ctx.put(Constants.STATUS, Constants.STATUS_RESPONSE_ACCEPTED);
                    return PREPARED | NO_JOIN;
                }
            } else {
                log.info("Failed to sent message to : '" + destination + "' because host is not connected");
                ctx.put(Constants.STATUS, Constants.STATUS_NOT_CONNECTED);
                return PREPARED | NO_JOIN;
            }
        } catch (ISOException ex) {
            log.error(Utility.getExceptionStackTraceAsString(ex));
            ctx.put(Constants.STATUS, Constants.STATUS_BAD_RESPONSE);
            return ABORTED | NO_JOIN;
        } catch (NameRegistrar.NotFoundException ex) {
            log.error(Utility.getExceptionStackTraceAsString(ex));
            ctx.put(Constants.STATUS, Constants.STATUS_NOT_CONNECTED);
            return PREPARED | NO_JOIN;
        }
    }

    @Override
    public void setConfiguration(Configuration c) throws ConfigurationException {
        this.cfg = c;
        destination = cfg.get("destination");
        mandatoryFields = cfg.get("mandatory");
        timeout = cfg.getLong("timeout", 30000);
    }

}
