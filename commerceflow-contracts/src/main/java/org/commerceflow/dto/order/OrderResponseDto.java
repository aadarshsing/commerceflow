package org.commerceflow.dto.order;


import org.commerceflow.enums.order.OrderStatus;

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
