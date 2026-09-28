package com.shipment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.commerceflow.dto.order.OrderAddressResponseDto;

public record CreateShipmentDto(
        @NotNull(message = "orderId cannot be null")
        Long orderId,
        @NotNull(message = "CustomerId cannot be null")
        Long customerId,
        @NotNull(message = "shippingAddress cannot be null")
        @Valid
        OrderAddressResponseDto shippingAddress
) {
}
