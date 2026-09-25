package com.order.config.feign;


import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PutMapping;

@Configuration
public class FeignRetryConfig {

    @Bean
    public Retryer retryer(){
        return new Retryer.Default(
                300,
                1500,
                3
        );
    }
    @Bean
    public ErrorDecoder errorDecoder(){
        return new RetryableErrorDecoder();
    }
}
