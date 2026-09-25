package org.commerceflow.dto.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.commerceflow.enums.inventory.InventoryOperation;

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
