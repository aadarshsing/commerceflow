package com.shipment.dto;

import com.shipment.entity.enums.ShipmentStatus;

public record CreateShipmentDto(
        Long orderId,
        Long customerId,
        OrderAddressResponseDto shippingAddress
) {
}
