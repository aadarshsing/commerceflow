package com.shipment.dto.shipment;

import com.shipment.dto.order.OrderAddressResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
