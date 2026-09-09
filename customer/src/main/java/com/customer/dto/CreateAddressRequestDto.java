package com.customer.dto;

import com.customer.entity.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAddressRequestDto(
        @NotNull(message = "customerId cannot be null,empty or blank")
        Long customerId,
        @NotNull(message = "Address type cannot be null,empty or blank")
        AddressType type,
        @NotBlank(message = "addressLine1 cannot be null,empty or blank")
        String addressLine1,
        String addressLine2,
        @NotBlank(message = "city cannot be null,empty or blank")
        String city,
        @NotBlank(message = "state cannot be null,empty or blank")
        String state,
        @NotBlank(message = "country cannot be null,empty or blank")
        String country,
        @NotBlank(message = "postalCode cannot be null,empty or blank")
        String postalCode,
        @NotBlank(message = "phoneNumber cannot be null,empty or blank")
        String phoneNumber,
        @NotNull(message = "isDefault cannot be null,empty or blank")
        Boolean isDefault
) {
}
