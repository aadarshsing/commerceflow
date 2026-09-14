package com.payment.service.implementation;

import com.payment.dto.CreatePaymentDto;
import com.payment.dto.OrderResponseDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.dto.ResponseDto;
import com.payment.entity.Payment;
import com.payment.entity.enums.OrderStatus;
import com.payment.entity.enums.PaymentMethod;
import com.payment.entity.enums.PaymentStatus;
import com.payment.exception.DuplicateResourceException;
import com.payment.exception.ResourceNotFoundException;
import com.payment.mapper.PaymentMapper;
import com.payment.repository.PaymentRepository;
import com.payment.service.IPaymentService;
import com.payment.service.client.OrderFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    PaymentRepository paymentRepository;
    OrderFeignClient orderFeignClient;
    @Override
    public ResponseDto createPayment(CreatePaymentDto createPaymentDto) {

        OrderResponseDto orderResponseDto = orderFeignClient.getOrder(createPaymentDto.orderId()).getBody();
        if(!orderResponseDto.orderStatus().equals(OrderStatus.CREATED)){
            throw new IllegalStateException("Order status is not valid as it is "+ orderResponseDto.orderStatus());
        }
        Optional<Payment> payment1 = paymentRepository.findByOrderIdAndPaymentStatus(createPaymentDto.orderId(), PaymentStatus.SUCCESS);
        if (payment1.isPresent()){
            throw new IllegalStateException("Payment already succeed find payment by id "+payment1.get().getId());
        }
        Payment payment = PaymentMapper.createPaymentDtoToPaymentMapper(createPaymentDto,new Payment());
        payment.setAmount(orderResponseDto.totalAmount());
        payment.setOrderId(orderResponseDto.id());
        payment.setCustomerId(orderResponseDto.customerId());
        paymentRepository.save(payment);
        return  new ResponseDto(
                HttpStatus.CREATED.toString(),
                PaymentStatus.SUCCESS
        );

    }

    @Override
    public Page<PaymentResponseDto> getPaymentByCustomer(Long customerId, int page, int size, PaymentStatus paymentStatus, PaymentMethod paymentMethod) {
        Sort sort = null;
        if (paymentStatus!=null){
            sort = Sort.by(
                    Sort.Direction.DESC,"paymentStatus"
            );
        }
        else if(paymentMethod!=null){
            sort = Sort.by(
                    Sort.Direction.DESC,"paymentMethod"
            );
        }
        else{
            sort = Sort.by(
                    Sort.Direction.DESC,"createdAt"
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );
        Page<Payment>payments = paymentRepository.findByCustomerId(customerId,pageable);
        return payments.map(PaymentMapper::paymentToPaymentResponseDto);
    }

    @Override
    public List<PaymentResponseDto> getPaymentByOrder(Long orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Payments","orderId",orderId.toString())
        );
        return payments.stream().map(PaymentMapper::paymentToPaymentResponseDto).toList();

    }

    @Override
    public PaymentResponseDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Payments","orderId",id.toString())
        );
        return PaymentMapper.paymentToPaymentResponseDto(payment);
    }

    @Override
    public ResponseDto updatePaymentStatus(Long id,PaymentStatus status) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Payments","orderId",id.toString())
        );
        if(isValidTransition(payment.getStatus(),status)){
            payment.setStatus(status);
            paymentRepository.save(payment);
            return new ResponseDto(
                    HttpStatus.OK.toString(),
                    status
            );
        }
        else{
            throw new IllegalStateException(
                    "Invalid payment status transition from "
                            + payment.getStatus() + " to " + status
            );
        }

    }

    @Override
    public ResponseDto cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Payments","orderId",id.toString())
        );
        if(isValidTransition(payment.getStatus(),PaymentStatus.CANCELLED)){
            payment.setStatus(PaymentStatus.CANCELLED);
            return new ResponseDto(
                    HttpStatus.OK.toString(),
                    PaymentStatus.CANCELLED
            );
        }
        else{
            throw new IllegalStateException(
                    "Invalid payment status transition from "
                            + payment.getStatus() + " to " + PaymentStatus.CANCELLED
            );
        }
    }

    public boolean isValidTransition(PaymentStatus currentStatus,
                                     PaymentStatus newStatus) {

        if (currentStatus == PaymentStatus.PENDING) {

            if (newStatus == PaymentStatus.SUCCESS ||
                    newStatus == PaymentStatus.FAILED ||
                    newStatus == PaymentStatus.CANCELLED) {
                return true;
            }

            return false;

        } else if (currentStatus == PaymentStatus.SUCCESS) {

            if (newStatus == PaymentStatus.REFUNDED) {
                return true;
            }

            return false;

        } else if (currentStatus == PaymentStatus.FAILED) {

            // Failed payment can be retried
            if (newStatus == PaymentStatus.PENDING) {
                return true;
            }

            return false;

        } else if (currentStatus == PaymentStatus.CANCELLED) {

            // Cancelled is a final state
            return false;

        } else if (currentStatus == PaymentStatus.REFUNDED) {

            // Refunded is a final state
            return false;
        }

        return false;
    }
}
