package com.mii.komi.exception;

import com.mii.komi.util.Utility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
@Component
public class RestTemplateResponseErrorHandler
        implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == CLIENT_ERROR
                || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {
        // modify : 
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            // handle SERVER_ERROR
            if (httpResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                if (httpResponse.getBody() != null) {
                    // suppress this error, handle outside catch
                    /*
                    InputStream is = httpResponse.getBody();
                    throw new HttpRequestException(Utility.convertInputStreamToString(is));
                    */
                }
            }
        } else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            // handle CLIENT_ERROR
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new DataNotFoundException();
            } else if (httpResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                if (httpResponse.getBody() != null) {
                    InputStream is = httpResponse.getBody();
                    throw new HttpRequestException(Utility.convertInputStreamToString(is));
                }
            }
        }
    }
}
