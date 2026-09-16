package com.order.controller;


import com.order.dto.order.BuyNowRequest;
import com.order.dto.cart.CartCheckOutRequest;
import com.order.dto.order.OrderResponseDto;
import com.order.dto.order.ResponseDto;
import com.order.entity.enums.OrderStatus;
import com.order.service.IOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class OrderController {

    @Autowired
    IOrderService orderService;

    @PostMapping("/orders/checkout")
    public ResponseEntity<ResponseDto>  createOrderFromCart(@Valid @RequestBody CartCheckOutRequest cartCheckOutRequest){

        orderService.createOrderFromCart(cartCheckOutRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(
                HttpStatus.CREATED.toString(),
                "Order is created Successfully"
        ));
    }

    @PostMapping("/orders/buy-now")
    public ResponseEntity<ResponseDto> createOrderFromProduct(@Valid @RequestBody BuyNowRequest buyNowRequest){

        orderService.createOrderFromBuyNow(buyNowRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(
                HttpStatus.CREATED.toString(),
                "Order is created Successfully"
        ));
    }
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @NotNull(message = "orderId cannot be null")
            @PathVariable Long id){
        OrderResponseDto orderResponseDto = orderService.getOrderById(id);
        return  ResponseEntity.ok(orderResponseDto);
    }
    @GetMapping("/orders/{orderId}/exist")
    public ResponseEntity<Boolean> checkOrder(
            @NotNull(message = "orderId cannot be null")
            @PathVariable Long orderId
    ){
        Boolean isPresent = orderService.checkOrder(orderId);
        return ResponseEntity.ok(isPresent);
    }
    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<Page<OrderResponseDto>> listOrdersByCustomer(
            @NotNull(message = "customerId cannot be null")
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        Page<OrderResponseDto> list = orderService.getOrdersByCustomerId(
                customerId,
                page,
                size
        );
        return ResponseEntity.ok(list);

    }

    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<ResponseDto> updateOrderStatus(
            @NotNull(message = "orderId cannot be null")
            @PathVariable
            Long orderId,
            @NotNull(message = "status cannot be null")
            @RequestParam
            OrderStatus orderStatus){
        orderService.makeOrderTransition(orderId, orderStatus);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDto(
                        HttpStatus.OK.toString(),
                        "Status Transition happened successfully"
                )
        );
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<ResponseDto> cancelOrder(
            @NotNull(message = "orderId cannot be null")
            @PathVariable
            Long orderId){
        orderService.makeOrderTransition(orderId, OrderStatus.CANCELLED);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDto(
                        HttpStatus.OK.toString(),
                        "Ordern cancelled successfully"
                )
        );
    }

}
