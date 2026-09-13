package com.payment.service;


import com.payment.dto.CreatePaymentDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.dto.ResponseDto;
import com.payment.entity.enums.PaymentMethod;
import com.payment.entity.enums.PaymentStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPaymentService {

    /**
     *
     * @param createPaymentDto
     */
    ResponseDto createPayment(CreatePaymentDto createPaymentDto);

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
