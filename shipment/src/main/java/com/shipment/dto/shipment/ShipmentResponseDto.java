package com.shipment.dto.shipment;

import com.shipment.entity.ShippingAddress;
import com.shipment.entity.enums.ShipmentStatus;

import java.time.Instant;

public record ShipmentResponseDto(
      Long id,
      Long orderId,
      Long customerId,
      String trackingNumber,
      String carrier,
      ShipmentStatus status,
      ShippingAddress shippingAddress,
      Instant estimatedDeliveryDate,
      Instant shippedAt,
      Instant deliveredAt
) {
}
