package com.shipment.dto;

public record ShipmentAddressResponseDto(
        Long id,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {
}
