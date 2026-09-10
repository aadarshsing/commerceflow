package com.inventory.dto;




import com.inventory.enitity.enums.ProductStatus;

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