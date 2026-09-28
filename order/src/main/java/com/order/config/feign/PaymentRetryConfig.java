package com.order.config.feign;


import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentRetryConfig {

    @Bean
    public Retryer paymentRetryer(){
        return new Retryer.Default(
                500,
                2000,
                3
        );
    }
    @Bean
    public ErrorDecoder paymentErrorDecoder(){
        return new RetryableErrorDecoder();
    }
}
