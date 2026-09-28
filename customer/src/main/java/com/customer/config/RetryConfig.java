package com.customer.config;


import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryConfig {

    @Bean
    public Retryer retryer(){
        return new Retryer.Default(
                400,
                2000,
                3
        );
    }
    public ErrorDecoder errorDecoder(){
        return new RetryableErrorDecoder();
    }
}
