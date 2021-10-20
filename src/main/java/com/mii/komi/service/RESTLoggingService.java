package com.mii.komi.service;

import com.mii.komi.util.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author vinch
 */
@Service
public class RESTLoggingService {
    
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public void log(Direction d, String content, String method, String remoteAddr, String requestUri, String reqRsp) {
        String contentsField = "query";
        if (method != null) {
            if (method.equalsIgnoreCase("GET") && d.equals(Direction.INCOMING)) {
                content = content;
            } else {
                contentsField = "payload";
            }
        } else {
            method = "ISO8583";
            contentsField = "payload";
        }

        String dir = "to";
        if (d.equals(Direction.INCOMING)) {
            dir = "from";
        }
        logger.info(d.toString() + " " + reqRsp + " " + method + " " + requestUri + " " + dir + " : " + remoteAddr + "," + contentsField + " :\n" + content);
    }
    
}
