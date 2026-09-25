package com.order.client;
import com.order.client.fallback.InventoryFallBack;
import com.order.config.feign.FeignRetryConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.commerceflow.dto.inventory.InventoryResponseDto;
import org.commerceflow.dto.inventory.UpdateInventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory"
        ,configuration = FeignRetryConfig.class
        ,fallback = InventoryFallBack.class)
public interface InventoryFeignClient {

    @PutMapping("api/inventories/{productId}/stock")
    ResponseEntity<InventoryResponseDto> updateStock(
            @NotNull(message = "productId cannot be null")
            @Positive(message = "productId must be greater than 0")
            @PathVariable Long productId,
            @NotEmpty(message = "idempotencyKey should not be null,empty or blank")
            @RequestParam String idempotencyKey,
            @NotNull(message = "callFromOrder cannot be null")
            @RequestParam Boolean callFromOrder,
            @Valid @RequestBody UpdateInventoryDto updateInventoryDto);
}
