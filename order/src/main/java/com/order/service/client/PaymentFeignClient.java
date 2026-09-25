package com.order.service.client;


import com.order.dto.ResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.commerceflow.dto.payment.CreatePaymentDto;
import org.commerceflow.dto.payment.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "payment")
public interface PaymentFeignClient {

    @PostMapping("api/payments")
    ResponseEntity<ResponseDto> createPayment(
            @NotNull(message = "paymentIdempotencyKey cannot be null")
            @RequestParam
            String paymentIdempotencyKey,
            @RequestBody CreatePaymentDto createPaymentDto);

    @GetMapping("api/orders/{orderId}/payments")
    ResponseEntity<List<PaymentResponseDto>> getPaymentByOrder(
            @NotNull(message = "order id cannot be null")
            @PathVariable Long orderId);

    @PostMapping("api/payments/{paymentId}/{orderId}/refund")
    ResponseEntity<ResponseDto> createRefundPayment(
            @NotNull(message = "payment id cannot be null")
            @PathVariable Long paymentId,
            @NotNull(message = "orderId cannot be null")
            @PathVariable Long orderId,
            @NotBlank(message = "refundPaymentIdempotencyKey cannot be blank empty or null")
            @RequestParam String refundPaymentIdempotencyKey);
}
