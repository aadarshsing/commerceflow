package com.cart.dto;

import jakarta.validation.constraints.NotNull;

public record CreateCartDto(
        @NotNull(message = "customerId cannot be null")
        Long customerId
) {
}
