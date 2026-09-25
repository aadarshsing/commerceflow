package com.order.client.fallback;

import com.order.dto.ResponseDto;
import com.order.exception.ServiceUnavailableException;
import com.order.client.PaymentFeignClient;
import org.commerceflow.dto.payment.CreatePaymentDto;
import org.commerceflow.dto.payment.PaymentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentFallBack implements PaymentFeignClient {
    @Override
    public ResponseEntity<ResponseDto> createPayment(String paymentIdempotencyKey, CreatePaymentDto createPaymentDto) {
        throw new ServiceUnavailableException(
                "Payment Service is currently unavailable"
        );
    }

    @Override
    public ResponseEntity<List<PaymentResponseDto>> getPaymentByOrder(Long orderId) {
        throw new ServiceUnavailableException(
                "Payment Service is currently unavailable"
        );
    }

    @Override
    public ResponseEntity<ResponseDto> createRefundPayment(Long paymentId, Long orderId, String refundPaymentIdempotencyKey) {
        throw new ServiceUnavailableException(
                "Payment Service is currently unavailable"
        );
    }
}
