package com.order.dto;


import com.order.entity.enums.AddressType;

public record AddressResponseDto(
        Long customerId,
        AddressType type,
        String addressLine1,
        String addressLine2, String city, String state, String country, String postalCode, String phoneNumber,
        Boolean isDefault
) {
}
