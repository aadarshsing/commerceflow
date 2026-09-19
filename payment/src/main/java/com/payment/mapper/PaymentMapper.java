package com.payment.mapper;


import com.payment.dto.payment.CreatePaymentDto;
import com.payment.dto.payment.PaymentResponseDto;
import com.payment.entity.Payment;
import com.payment.entity.enums.payment.PaymentStatus;

import java.util.UUID;

public class PaymentMapper {


    public static Payment createPaymentDtoToPaymentMapper(CreatePaymentDto paymentDto, Payment payment){
        payment.setOrderId(paymentDto.orderId());
        payment.setCustomerId(paymentDto.customerId());
        payment.setPaymentMethod(paymentDto.paymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
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
