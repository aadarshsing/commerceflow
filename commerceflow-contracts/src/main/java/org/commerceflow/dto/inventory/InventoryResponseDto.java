package org.commerceflow.dto.inventory;


import org.commerceflow.enums.inventory.InventoryStatus;

public record InventoryResponseDto(
        Long productId,
        int availableQuantity,
        int reservedQuantity,
        int lowStockThreshold,
        InventoryStatus status
) {
}
