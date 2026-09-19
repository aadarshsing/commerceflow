package org.commerceflow.dto.catalog;



import org.commerceflow.enums.catalog.ProductStatus;

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