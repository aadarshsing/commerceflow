package org.commerceflow.dto.payment;


import org.commerceflow.enums.payment.PaymentMethod;
import org.commerceflow.enums.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponseDto(
        Long paymentId,
        Long orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        PaymentStatus status,
        String transactionReference,
        Instant createdAt,
        Instant updatedAt
) {
}
