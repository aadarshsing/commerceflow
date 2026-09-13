package com.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BuyNowRequest(
        @NotNull(message = "customerId cannot be  null")
        Long customerId,
        @NotNull(message = "productId cannot be  null")
        Long productId,
        @Min(value = 1,message = "quantity must be greater than 0")
        int quantity,
        @NotNull(message = "shippingAddressId cannot be  null")
        Long shippingAddressId
) {
}
