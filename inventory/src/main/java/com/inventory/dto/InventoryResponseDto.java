package com.inventory.dto;


import com.inventory.enitity.enums.InventoryStatus;

public record InventoryResponseDto(
        Long productId,
        int availableQuantity,
        int reservedQuantity,
        int lowStockThreshold,
        InventoryStatus inventoryStatus
) {
}
