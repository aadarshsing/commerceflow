package com.order.dto.order;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long productId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
