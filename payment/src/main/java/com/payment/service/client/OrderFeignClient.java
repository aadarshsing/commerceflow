package com.payment.service.client;

import jakarta.validation.constraints.NotNull;
import org.commerceflow.dto.order.OrderResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order")
public interface OrderFeignClient {

    @GetMapping("api/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @NotNull(message = "orderId cannot be null")
            @PathVariable Long id);
}
