package com.order.service.client;


import com.order.dto.CreatePaymentDto;
import com.order.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment")
public interface PaymentFeignClient {

    @PostMapping("api/payments")
    ResponseEntity<ResponseDto> createPayment(@RequestBody CreatePaymentDto createPaymentDto);
}
