package com.mii.komi.jpos.participant.inbound;

import com.mii.komi.util.Constants;
import java.io.Serializable;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.transaction.Context;
import org.jpos.transaction.GroupSelector;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
public class RESTSelector implements GroupSelector, Configurable {
    
    private Configuration configuration;
    
    @Override
    public String select(long l, Serializable srlzbl) {
        Context ctx = (Context) srlzbl;
        String selector = configuration.get(ctx.getString(Constants.SELECTOR_KEY));
        return selector;
    }

    @Override
    public int prepare(long id, Serializable context) {
        return PREPARED | ABORTED | NO_JOIN;
    }

    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        this.configuration = configuration;
    }
    
}
