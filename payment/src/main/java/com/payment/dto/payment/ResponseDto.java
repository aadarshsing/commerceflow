package com.payment.dto.payment;

import com.payment.entity.enums.payment.PaymentStatus;

public record ResponseDto(
        String statusCode,
        PaymentStatus statusMsg
) {}
