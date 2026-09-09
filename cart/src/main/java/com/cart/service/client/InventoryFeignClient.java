package com.cart.service.client;

import com.cart.dto.InventoryResponseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory")
public interface InventoryFeignClient {

    @GetMapping("/inventories/{productId}")
    ResponseEntity<InventoryResponseDto> getInventory(
            @NotNull(message = "productId cannot be null")
            @Positive(message = "productId must be greater than 0")
            @PathVariable Long productId);
}
