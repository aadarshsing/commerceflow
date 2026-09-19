package org.commerceflow.dto.order;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long productId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
