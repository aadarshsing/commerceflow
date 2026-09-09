package com.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCartItemDto(
        @NotNull(message = "productId cannot be null")
        Long  productId,
        @Min(
                value = 1,
                message = "Product quantity must be greater than zero"
        )
        int quantity
) {
}
