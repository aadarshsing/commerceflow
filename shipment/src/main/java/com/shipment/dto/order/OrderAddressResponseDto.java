package com.shipment.dto.order;


public record OrderAddressResponseDto(
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {
}
