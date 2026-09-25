package com.inventory.dto.inventory;

import com.inventory.enitity.enums.InventoryOperation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateInventoryDto(
        @Min(
                value = 1,
                message = "Quantity must be greater than 0"
        )
        int quantity,
        @NotNull(message = "Operation cannot be empty")
        InventoryOperation operation,
        @NotNull(message = "OrderId cannot be null")
        Long OrderId
) {
}
