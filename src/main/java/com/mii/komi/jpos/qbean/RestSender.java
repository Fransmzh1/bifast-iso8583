package com.mii.komi.jpos.qbean;

import com.mii.komi.exception.RestTemplateResponseErrorHandler;
import java.util.Arrays;
import org.jpos.q2.QBeanSupport;
import org.jpos.util.NameRegistrar;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vinch
 */
public class RestSender extends QBeanSupport {

    private RestTemplate restTemplate;

    @Override
    protected void initService() {
        NameRegistrar.register(getName(), this);
    }

    @Override
    protected void startService() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    public HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + cfg.get("authorization"));
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    /**
     * @return the restTemplate
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     * @param restTemplate the restTemplate to set
     */
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
