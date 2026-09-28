package com.payment.dto;

import com.payment.entity.enums.payment.PaymentStatus;

public record ResponseDto(
        String statusCode,
        String statusMsg
) {}
