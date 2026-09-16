package com.order.dto.cart;

import jakarta.validation.constraints.NotNull;

public record CartCheckOutRequest(
        @NotNull(message = "customerId cannot be  null")
        Long customerId,
        @NotNull(message = "cartId cannot be null")
        Long cartId,
        @NotNull(message = "shippingAddressId cannot be null")
        Long shippingAddressId
) {
}
