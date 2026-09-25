package com.order.config.feign;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RetryableErrorDecoder implements ErrorDecoder {

    private  final  ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String s, Response response) {
        if(response.status() == 502 || response.status() == 503 || response.status() == 504){
            return new RetryableException(
                    response.status(),
                    "Retryable HTTP error: " + response.status(),
                    response.request().httpMethod(),
                    (Date) null,
                    response.request()
            );
        }
        return errorDecoder.decode(s,response);
    }
}
