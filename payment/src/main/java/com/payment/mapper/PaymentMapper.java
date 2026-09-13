package com.payment.mapper;


import com.payment.dto.CreatePaymentDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.entity.Payment;
import com.payment.entity.enums.PaymentStatus;

import java.util.UUID;

public class PaymentMapper {


    public static Payment createPaymentDtoToPaymentMapper(CreatePaymentDto paymentDto, Payment payment){
        payment.setPaymentMethod(paymentDto.paymentMethod());
        payment.setStatus(PaymentStatus.PENDING);
        String transactionReference = "TXN-" + UUID.randomUUID();
        payment.setTransactionReference(transactionReference);
        return payment;
    }

    public  static PaymentResponseDto paymentToPaymentResponseDto(Payment payment){
        return new PaymentResponseDto(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionReference(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
