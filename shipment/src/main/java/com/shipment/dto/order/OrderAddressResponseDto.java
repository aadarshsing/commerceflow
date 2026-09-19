package com.shipment.dto.order;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OrderAddressResponseDto(
        @NotBlank(message = "Address Line 1 is required")
        String addressLine1,

        String addressLine2, // Optional

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "State is required")
        String state,

        @NotBlank(message = "Country is required")
        String country,

        @NotBlank(message = "Postal code is required")
        String postalCode,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[0-9]{10}$",
                message = "Phone number must contain exactly 10 digits"
        )
        String phoneNumber
) {
}
