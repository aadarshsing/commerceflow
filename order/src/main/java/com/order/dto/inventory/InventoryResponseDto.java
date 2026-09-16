package com.order.dto.inventory;


import com.order.entity.enums.InventoryStatus;

public record InventoryResponseDto(
        Long productId,
        int availableQuantity,
        int reservedQuantity,
        int lowStockThreshold,
        InventoryStatus status
) {
}
