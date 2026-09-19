package com.order.dto.shipment;


import com.order.dto.order.OrderAddressResponseDto;

public record CreateShipmentDto(
        Long orderId,
        Long customerId,
        OrderAddressResponseDto shippingAddress
) {
}
