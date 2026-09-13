package com.payment.dto;

import com.payment.entity.enums.PaymentStatus;

public record ResponseDto(
        String statusCode,
        PaymentStatus statusMsg
) {}
