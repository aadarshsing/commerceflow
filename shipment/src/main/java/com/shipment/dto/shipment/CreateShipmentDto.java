package com.shipment.dto.shipment;

import com.shipment.dto.order.OrderAddressResponseDto;

public record CreateShipmentDto(
        Long orderId,
        Long customerId,
        OrderAddressResponseDto shippingAddress
) {
}
