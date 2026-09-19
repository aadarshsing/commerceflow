package com.payment.dto.payment;

import com.payment.entity.enums.payment.PaymentMethod;
import com.payment.entity.enums.payment.PaymentStatus;

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
