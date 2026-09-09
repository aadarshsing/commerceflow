package com.catalog.product.dto;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequestDto(

        @NotNull(message = "sellerId cannot be null")
        Long sellerId,

        @NotNull(message = "categoryId cannot be null")
        Long categoryId,

        @NotNull(message = "name cannot be null")
        @Size(min = 1 , max = 30,message = "name must be between 1 and 30 character")
        String name,
        String description,

        @NotNull(message = "sku cannot be null")
        @NotBlank(message = "sku cannot be blank")
        String sku,

        @NotNull(message = "Product price must be present and greater than zero")
        @DecimalMin(
                value = "0",
                inclusive = false,
                message = "Product price must be greater than zero"
        )
        BigDecimal price,
        @Min(
                value = 1,
                message = "Product quantity must be greater than zero"
        )
        int availableQuantity,
        @Min(
                value = 1,
                message = "Product low stock threshold must be greater than zero"
        )
        int lowStockThreshold
) {
}
