package com.payment.service.client;

import com.payment.dto.PaymentResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.commerceflow.dto.order.OrderResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order")
public interface OrderFeignClient {

    @GetMapping("api/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @NotNull(message = "orderId cannot be null")
            @PathVariable Long id);

    @PostMapping("api/orders/{orderId}/confirm-cancel")
    public ResponseEntity<OrderResponseDto> confirmOrCancelOrder(
            @NotNull(message = "orderId cannot be null")
            @PathVariable
            Long orderId,
            @RequestBody
            PaymentResponseDto paymentResponseDto,
            @NotBlank(message = "idempotencyKey cannot be null, empty or blank")
            @RequestParam
            String idempotencyKey
    );
}
