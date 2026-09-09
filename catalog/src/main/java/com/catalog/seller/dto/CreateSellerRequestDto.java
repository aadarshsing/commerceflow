package com.catalog.seller.dto;

import com.catalog.seller.entity.enums.SellerStatus;
import jakarta.validation.constraints.*;

public record CreateSellerRequestDto(

        @NotEmpty(message = "BusninessName cannot be null or empty")
        @Size(min = 5,max = 30,message = "The length of BusinessName should be between 5 to 30")
        String  businessName,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid Email Format")
        String email,

        @NotEmpty(message = "Mobile number cannot be null or empty")
        @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
        String phone,
        SellerStatus status
) {
}
