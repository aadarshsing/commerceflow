package com.order.dto;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long productId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
