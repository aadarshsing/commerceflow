package com.customer.dto.customer;



import com.customer.dto.address.AddressResponseDto;
import com.customer.entity.enums.CustomerStatus;

import java.util.List;

public record CustomerResponseDto(
        Long id,
        String name,
        String email,
        String phoneNumber,
        CustomerStatus customerStatus,
        List<AddressResponseDto> customerAddress
) {
}
