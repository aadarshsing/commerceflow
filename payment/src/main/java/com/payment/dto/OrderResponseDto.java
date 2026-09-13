package com.payment.dto;

import com.payment.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Long customerId,
        OrderStatus orderStatus,
        BigDecimal totalAmount,
        OrderAddressResponseDto orderAddressResponseDto,
        List<OrderItemResponseDto> orderItems,
        Instant createdDate
) {
}
