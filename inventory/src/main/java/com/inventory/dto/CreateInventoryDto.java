package com.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateInventoryDto(
        @NotNull(message = "productId cannot be null")
        @Positive(message = "productId must be greater than 0")
        Long productId,
        @Min(
                value = 1,
                message = "Available quantity must be greater than  0"
        )
        int availableQuantity,
        @Min(
                value = 1,
                message = "low stock threshold must be greater than 0"
        )
        int lowStockThreshold
) {
}
