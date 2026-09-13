package com.order.dto;




import com.order.entity.enums.CustomerStatus;

import java.util.List;

public record CustomerResponseDto(
        String name,
        String email,
        String phoneNumber,
        CustomerStatus status,
        List<AddressResponseDto> customerAddress
) {
}
