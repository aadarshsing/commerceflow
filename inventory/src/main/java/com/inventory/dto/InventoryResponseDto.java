package com.inventory.dto;


import com.inventory.enitity.enums.Status;

public record InventoryResponseDto(
        Long productId,
        int availableQuantity,
        int reservedQuantity,
        int lowStockThreshold,
        Status status
) {
}
