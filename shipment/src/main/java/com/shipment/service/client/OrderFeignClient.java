package com.shipment.service.client;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order")
public interface OrderFeignClient {

    @GetMapping("api/orders/{orderId}/exist")
    public ResponseEntity<Boolean> checkOrder(
            @NotNull(message = "orderId cannot be null")
            @PathVariable Long orderId
    );
}
