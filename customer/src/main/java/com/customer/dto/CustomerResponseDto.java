package com.customer.dto;



import com.customer.entity.enums.Status;

import java.util.List;

public record CustomerResponseDto(
        String name,
        String email,
        String phoneNumber,
        Status status,
        List<AddressResponseDto> customerAddress
) {
}
