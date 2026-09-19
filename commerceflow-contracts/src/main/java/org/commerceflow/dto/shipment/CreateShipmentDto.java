package org.commerceflow.dto.shipment;


import org.commerceflow.dto.order.OrderAddressResponseDto;

public record CreateShipmentDto(
        Long orderId,
        Long customerId,
        OrderAddressResponseDto shippingAddress
) {
}
