package com.cart.dto.inventory;


import com.cart.entity.enums.InventoryStatus;

public record InventoryResponseDto(
        Long productId,
        int availableQuantity,
        int reservedQuantity,
        int lowStockThreshold,
        InventoryStatus status
) {
}
