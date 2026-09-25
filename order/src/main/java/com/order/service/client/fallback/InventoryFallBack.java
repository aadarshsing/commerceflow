package com.order.service.client.fallback;

import com.order.exception.ServiceUnavailableException;
import com.order.service.client.InventoryFeignClient;
import org.commerceflow.dto.inventory.InventoryResponseDto;
import org.commerceflow.dto.inventory.UpdateInventoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryFallBack implements InventoryFeignClient {
    @Override
    public ResponseEntity<InventoryResponseDto> updateStock(Long productId, String idempotencyKey, Boolean callFromOrder, UpdateInventoryDto updateInventoryDto) {
        throw new ServiceUnavailableException(
                "Inventory Service is currently unavailable"
        );
    }
}
