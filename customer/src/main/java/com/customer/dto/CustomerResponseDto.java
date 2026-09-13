package com.customer.dto;



import com.customer.entity.enums.CustomerStatus;

import java.util.List;

public record CustomerResponseDto(
        String name,
        String email,
        String phoneNumber,
        CustomerStatus customerStatus,
        List<AddressResponseDto> customerAddress
) {
}
