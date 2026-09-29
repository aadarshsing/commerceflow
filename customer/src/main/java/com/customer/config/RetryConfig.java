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
                200,
                1000,
                3
        );
    }
    @Bean
    public ErrorDecoder errorDecoder(){
        return new RetryableErrorDecoder();
    }
}
