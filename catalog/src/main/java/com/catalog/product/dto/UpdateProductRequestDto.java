package com.catalog.product.dto;

import com.catalog.product.entity.enums.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequestDto(
        @NotNull(message = "categoryId cannot be null")
        Long categoryId,

        @NotNull(message = "name cannot be null")
        @Size(min = 1 , max = 30,message = "name must be between 1 and 30 character")
        String name,
        String description,

        @DecimalMin(value = "1", message = "price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Status cannot be null")
        ProductStatus status
) {
}
