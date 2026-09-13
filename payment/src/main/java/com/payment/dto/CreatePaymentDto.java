package com.payment.dto;

import com.payment.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentDto(
        @NotNull(message = "orderId cannot be null")
        Long orderId,
        @NotNull(message = "customerId cannot be null")
        Long customerId,
        @NotNull(message = "paymentMethod cannot be null")
        PaymentMethod paymentMethod
) {
}
