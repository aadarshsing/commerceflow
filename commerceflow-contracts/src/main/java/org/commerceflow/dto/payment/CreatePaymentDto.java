package org.commerceflow.dto.payment;

import jakarta.validation.constraints.NotNull;
import org.commerceflow.enums.payment.PaymentMethod;

public record CreatePaymentDto(
        @NotNull(message = "orderId cannot be null")
        Long orderId,
        @NotNull(message = "customerId cannot be null")
        Long customerId,
        @NotNull(message = "paymentMethod cannot be null")
        PaymentMethod paymentMethod
) {
}
