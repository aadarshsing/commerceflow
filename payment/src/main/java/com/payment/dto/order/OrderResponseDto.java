package com.payment.dto.order;

import com.payment.entity.enums.order.OrderStatus;

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
