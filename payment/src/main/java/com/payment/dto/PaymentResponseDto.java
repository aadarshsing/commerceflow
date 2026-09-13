package com.payment.dto;

import com.payment.entity.enums.PaymentMethod;
import com.payment.entity.enums.PaymentStatus;

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
