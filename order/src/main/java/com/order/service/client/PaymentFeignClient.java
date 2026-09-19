package com.order.service.client;


import com.order.dto.payment.CreatePaymentDto;
import com.order.dto.order.ResponseDto;
import com.order.dto.payment.PaymentResponseDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "payment")
public interface PaymentFeignClient {

    @PostMapping("api/payments")
    ResponseEntity<ResponseDto> createPayment(@RequestBody CreatePaymentDto createPaymentDto);
    @GetMapping("api/orders/{orderId}/payments")
    ResponseEntity<List<PaymentResponseDto>> getPaymentByOrder(
            @NotNull(message = "order id cannot be null")
            @PathVariable Long orderId);
    @PostMapping("api/payments/{paymentId}/{orderId}/refund")
    ResponseEntity<ResponseDto> createRefundPayment(@PathVariable Long paymentId,@PathVariable Long orderId);
}
