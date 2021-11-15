package com.mii.komi.jpos.filter;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOFilter;
import org.jpos.iso.ISOMsg;
import org.jpos.util.Dumpable;
import org.jpos.util.LogEvent;

/**
 *
 * @author vinch
 */
public class IncomingOutgoingDebugFilter implements ISOFilter {

    @Override
    public ISOMsg filter(ISOChannel isoc, ISOMsg isomsg, LogEvent evt) throws VetoException {
        if (evt != null) {
            if (isomsg != null) {
                try {
                    evt.addMessage(new Dumpable("image", isomsg.pack()));
                } catch (ISOException ex) {
                    Logger.getLogger(IncomingOutgoingDebugFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return isomsg;
    }

}
