package com.mii.komi.jpos;

import com.mii.komi.util.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;

/**
 *
 * @author vinch
 */
public class ISORequestHandler implements ISORequestListener, Configurable {

    private Configuration cfg;

    @Override
    public boolean process(ISOSource isos, ISOMsg isoMsg) {
        Long spaceTimeout = cfg.getLong("timeout");
        String queueName = cfg.get("queue");
        Context context = new Context();
        try {
            if (isoMsg.isRequest()) {
                context.put(Constants.ISO_REQUEST, isoMsg);
                context.put(Constants.SOURCE_KEY, isos);
                Space space = SpaceFactory.getSpace();
                space.out(queueName, context, spaceTimeout);
            }
        } catch (ISOException ex) {
            Logger.getLogger(ISORequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public void setConfiguration(Configuration c) throws ConfigurationException {
        this.cfg = c;
    }

}
