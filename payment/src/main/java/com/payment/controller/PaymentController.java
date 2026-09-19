package com.payment.controller;


import com.payment.dto.payment.CreatePaymentDto;
import com.payment.dto.payment.PaymentResponseDto;
import com.payment.dto.payment.ResponseDto;
import com.payment.entity.enums.payment.PaymentMethod;
import com.payment.entity.enums.payment.PaymentStatus;
import com.payment.service.IPaymentService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api")
@Validated
public class PaymentController {

    @Autowired
    IPaymentService paymentService;


    @PostMapping("/payments")
    ResponseEntity<ResponseDto> createPayment(@RequestBody CreatePaymentDto createPaymentDto){

        ResponseDto responseDto = paymentService.createPayment(createPaymentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @PostMapping("/payments/{paymentId}/{orderId}/refund")
    ResponseEntity<ResponseDto> createRefundPayment(@PathVariable Long paymentId,@PathVariable Long orderId){

        ResponseDto responseDto = paymentService.createRefundPayment(paymentId,orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/payments/{paymentId}")
    ResponseEntity<PaymentResponseDto> getPaymentById(
            @NotNull(message = "payment id cannot be null")
            @PathVariable Long paymentId){
         PaymentResponseDto paymentResponseDto = paymentService.getPaymentById(paymentId);
         return ResponseEntity.ok(paymentResponseDto);
    }

    @GetMapping("/orders/{orderId}/payments")
    ResponseEntity<List<PaymentResponseDto>> getPaymentByOrder(
            @NotNull(message = "order id cannot be null")
            @PathVariable Long orderId){
        List<PaymentResponseDto> paymentResponseDtoList = paymentService.getPaymentByOrder(orderId);
        return ResponseEntity.ok(paymentResponseDtoList);
    }

    @PatchMapping("/payments/{paymentId}/status")
    ResponseEntity<ResponseDto> updatePaymentStatus(
            @NotNull(message = "payment id cannot be null")
            @PathVariable Long paymentId,
            @NotNull(message = "Payment status cannot be null")
            @RequestParam PaymentStatus status){

        ResponseDto responseDto = paymentService.updatePaymentStatus(paymentId,status);
        return ResponseEntity.ok(responseDto);

    }
    @GetMapping("/customers/{customerId}/payments")
    ResponseEntity<Page<PaymentResponseDto>> getCustomerPayments(
            @NotNull(message = "customerId cannot be null")
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) PaymentMethod paymentMethod){
        Page<PaymentResponseDto> paymentResponseDtoList = paymentService.getPaymentByCustomer(
                customerId,page,size,paymentStatus,paymentMethod);

        return ResponseEntity.ok(paymentResponseDtoList);
    }

    @PostMapping("/payments/{paymentId}/cancel")
    ResponseEntity<ResponseDto> cancelPayment(
            @NotNull(message = "Payment id cannot be null")
            @PathVariable Long paymentId){
        ResponseDto responseDto = paymentService.cancelPayment(paymentId);
        return ResponseEntity.ok(responseDto);
    }

}
