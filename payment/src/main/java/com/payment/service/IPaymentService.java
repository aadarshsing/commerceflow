package com.payment.service;


import com.payment.dto.payment.CreatePaymentDto;
import com.payment.dto.payment.PaymentResponseDto;
import com.payment.dto.payment.ResponseDto;
import com.payment.entity.enums.payment.PaymentMethod;
import com.payment.entity.enums.payment.PaymentStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPaymentService {

    /**
     *
     * @param paymentIdempotencyKey
     * @param createPaymentDto
     */
    ResponseDto createPayment(String paymentIdempotencyKey,CreatePaymentDto createPaymentDto);

    /**
     *
     * @param paymentId
     * @param orderId
     * @param refundPaymentIdempotencyKey
     * @return
     */
    ResponseDto createRefundPayment(Long paymentId,Long orderId,String refundPaymentIdempotencyKey);
    /**
     *
     * @param customerId
     * @param page
     * @param size
     * @param paymentStatus
     * @param paymentMethod
     * @return
     */
    Page<PaymentResponseDto> getPaymentByCustomer(
            Long customerId,
            int page,
            int size,
            PaymentStatus paymentStatus,
            PaymentMethod paymentMethod
            );

    /**
     *
     * @param orderId
     * @return
     */
    List<PaymentResponseDto> getPaymentByOrder(Long orderId);

    /**
     *
     * @param id
     * @return
     */
    PaymentResponseDto getPaymentById(Long id);

    /**
     *
     * @param id
     * @return
     */
    ResponseDto updatePaymentStatus(Long id,PaymentStatus status);

    /**
     *
     * @param id
     * @return
     */
    ResponseDto cancelPayment(Long id);


}
