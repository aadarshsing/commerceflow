package com.customer.dto;

import jakarta.validation.constraints.*;

public record UpdateCustomerDto(
        @NotEmpty(message = "Name cannot be null or empty")
        @Size(min = 5,max = 30,message = "The length of customer name should be between 5 to 30")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
                message = "Password must be at least 8 characters and contain at least 1 uppercase, 1 lowercase, 1 number, and 1 special character"
        )
        String password,

        @NotEmpty(message = "Mobile number cannot be null or empty")
        @Pattern(regexp = "(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
        String phoneNumber
) {
}
