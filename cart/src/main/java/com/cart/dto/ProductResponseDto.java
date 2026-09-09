package com.cart.dto;



import com.cart.entity.enums.ProductStatus;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        Long sellerId,
        Long categoryId,
        String name,
        String description,
        String sku,
        BigDecimal price,
        ProductStatus status
) {
}