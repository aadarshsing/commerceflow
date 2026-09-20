package com.payment.service.implementation;

import com.payment.dto.payment.CreatePaymentDto;
import com.payment.dto.payment.PaymentResponseDto;
import com.payment.dto.payment.ResponseDto;
import com.payment.entity.Payment;
import com.payment.entity.enums.payment.PaymentMethod;
import com.payment.entity.enums.payment.PaymentStatus;
import com.payment.exception.ResourceNotFoundException;
import com.payment.mapper.PaymentMapper;
import com.payment.repository.PaymentRepository;
import com.payment.service.IPaymentService;
import com.payment.service.client.NotificationFeignClient;
import com.payment.service.client.OrderFeignClient;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.commerceflow.dto.notification.CreateNotificationDto;
import org.commerceflow.dto.order.OrderResponseDto;
import org.commerceflow.enums.notification.NotificationType;
import org.commerceflow.enums.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    PaymentRepository paymentRepository;
    OrderFeignClient orderFeignClient;
    NotificationFeignClient notificationFeignClient;
    @Override
    public ResponseDto createPayment(CreatePaymentDto createPaymentDto) {

        try {
            OrderResponseDto orderResponseDto = orderFeignClient.getOrder(createPaymentDto.orderId()).getBody();
            if(!(OrderStatus.CREATED).equals(orderResponseDto.orderStatus())){
                throw new IllegalStateException("Order status is not valid as it is "+ orderResponseDto.orderStatus());
            }
            if(!orderResponseDto.customerId().equals(createPaymentDto.customerId())){
                throw new IllegalArgumentException("Order is not associated with given Customer "+createPaymentDto.customerId());
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
            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            createPaymentDto.customerId(),
                            NotificationType.PAYMENT_SUCCESS,
                            "SMS",
                            "Order",
                            "Payment is Success",
                            createPaymentDto.orderId().toString()
                    )
            );
        } catch (IllegalStateException e) {
            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            createPaymentDto.customerId(),
                            NotificationType.PAYMENT_FAILED,
                            "SMS",
                            "Order",
                            "Payment Failed because" + e,
                            createPaymentDto.orderId().toString()
                    )
            );
            throw new RuntimeException("Payment cannot created");
        } catch (IllegalArgumentException e) {
            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            createPaymentDto.customerId(),
                            NotificationType.PAYMENT_FAILED,
                            "SMS",
                            "Order",
                            "Payment Failed because" + e,
                            createPaymentDto.orderId().toString()
                    )
            );
            throw new RuntimeException("Payment cannot created");
        }
        catch (Exception e){
            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            createPaymentDto.customerId(),
                            NotificationType.PAYMENT_FAILED,
                            "SMS",
                            "Order",
                            "Payment Failed because" + e,
                            createPaymentDto.orderId().toString()
                    )
            );
            throw new RuntimeException("Payment cannot created");

        }
        return  new ResponseDto(
                HttpStatus.CREATED.toString(),
                PaymentStatus.SUCCESS.toString()
        );

    }

    @Override
    public ResponseDto createRefundPayment(Long paymentId, Long orderId) {

        Payment payment1 = paymentRepository.findByOrderIdAndPaymentStatus(orderId, PaymentStatus.SUCCESS).orElseThrow(
                ()-> new ResourceNotFoundException("Payment","PaymentId & PaymentStatus_Success",paymentId.toString() +" , "+PaymentStatus.SUCCESS)
        );

        if(!payment1.getId().equals(paymentId)){
            throw new IllegalArgumentException("Payment is not associated with given Order "+orderId);
        }
        Payment payment = PaymentMapper.createPaymentDtoToPaymentMapper(
                new CreatePaymentDto(
                        orderId,
                        payment1.getCustomerId(),
                        payment1.getPaymentMethod()
                )
                ,new Payment());
        payment.setAmount(payment1.getAmount());
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
        notificationFeignClient.createNotification(
                new CreateNotificationDto(
                       payment1.getCustomerId(),
                        NotificationType.PAYMENT_REFUND,
                        "SMS",
                        "Order",
                        "Payment is Refunded to original Payment Account",
                        paymentId.toString()
                )
        );
        return  new ResponseDto(
                HttpStatus.CREATED.toString(),
                PaymentStatus.REFUNDED.toString()
        );
    }

    @Override
    public Page<PaymentResponseDto> getPaymentByCustomer(Long customerId, int page, int size, PaymentStatus paymentStatus, PaymentMethod paymentMethod) {
        Sort sort = null;
        if (paymentStatus!=null){
            sort = Sort.by(
                    Sort.Direction.DESC,"status"
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
        if(payments.isEmpty()){
            throw new ResourceNotFoundException("Payments","CustomerId",customerId.toString());
        }
        return payments.map(PaymentMapper::paymentToPaymentResponseDto);
    }

    @Override
    public List<PaymentResponseDto> getPaymentByOrder(Long orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Payments","orderId",orderId.toString())
        );
        if(payments.isEmpty()){
            throw new ResourceNotFoundException("Payments","orderId",orderId.toString());
        }
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
        Payment payment1 = paymentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Payments","orderId",id.toString())
        );
        Optional<Payment> paymentWithStatus = paymentRepository.findByIdAndStatus(id,status);
        if(paymentWithStatus.isPresent()){
            throw new IllegalStateException("Payment already Exist with given Status "+ status.toString());
        }
        if(isValidTransition(payment1.getStatus(),status)){
            Payment payment = payment1.toBuilder()
                    .id(null)
                    .status(status)
                    .transactionReference("TXN-" + UUID.randomUUID())
                    .build();
            paymentRepository.save(payment);
            NotificationType notificationType = switch (status){
                case SUCCESS -> NotificationType.PAYMENT_SUCCESS;
                case PENDING -> NotificationType.PAYMENT_PENDING;
                case FAILED -> NotificationType.PAYMENT_FAILED;
                case REFUNDED -> NotificationType.PAYMENT_REFUND;
                case CANCELLED -> NotificationType.PAYMENT_CANCELLED;

            };
            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            payment1.getCustomerId(),
                            notificationType,
                            "SMS",
                            "Order",
                            "Payment is Success",
                            payment1.getOrderId().toString()
                    )
            );

            return new ResponseDto(
                    HttpStatus.OK.toString(),
                    status.toString()
            );
        }
        else{
            throw new IllegalStateException(
                    "Invalid payment status transition from "
                            + payment1.getStatus() + " to " + status
            );
        }

    }

    @Override
    public ResponseDto cancelPayment(Long id) {
        Payment payment1 = paymentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Payments","orderId",id.toString())
        );

        Optional<Payment> cancelledPayment = paymentRepository.findByIdAndStatus(id,PaymentStatus.CANCELLED);
        if(cancelledPayment.isPresent()){
            throw new IllegalStateException("Payment already Exist with given Status "+ PaymentStatus.CANCELLED);
        }
        if(isValidTransition(payment1.getStatus(),PaymentStatus.CANCELLED)){

            Payment payment = payment1.toBuilder()
                    .id(null)
                    .status(PaymentStatus.CANCELLED)
                    .transactionReference("TXN-" + UUID.randomUUID())
                    .build();
            paymentRepository.save(payment);

            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            payment1.getCustomerId(),
                            NotificationType.PAYMENT_CANCELLED,
                            "SMS",
                            "Order",
                            "Payment is Success",
                            payment1.getOrderId().toString()
                    )
            );

            return new ResponseDto(
                    HttpStatus.OK.toString(),
                    PaymentStatus.CANCELLED.toString()
            );
        }
        else{
            throw new IllegalStateException(
                    "Invalid payment status transition from "
                            + payment1.getStatus() + " to " + PaymentStatus.CANCELLED
            );
        }
    }

    public boolean isValidTransition(PaymentStatus currentStatus,
                                     PaymentStatus status) {

        if (currentStatus == PaymentStatus.PENDING) {

            if (status == PaymentStatus.SUCCESS ||
                    status == PaymentStatus.FAILED ||
                    status == PaymentStatus.CANCELLED) {
                return true;
            }

            return false;

        } else if (currentStatus == PaymentStatus.SUCCESS) {

            if (status == PaymentStatus.REFUNDED) {
                return true;
            }

            return false;

        } else if (currentStatus == PaymentStatus.FAILED) {

            if (status == PaymentStatus.PENDING) {
                return true;
            }

            return false;

        } else if (currentStatus == PaymentStatus.CANCELLED) {


            return false;

        } else if (currentStatus == PaymentStatus.REFUNDED) {

            return false;
        }

        return false;
    }
}
