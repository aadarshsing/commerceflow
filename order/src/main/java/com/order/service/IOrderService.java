package com.order.service;


import com.order.dto.BuyNowRequest;
import com.order.dto.CartCheckOutRequest;
import com.order.dto.OrderResponseDto;
import com.order.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {


    /**
     *
     * @param id
     * @return : it returns the order given by id
     */
    OrderResponseDto getOrderById(Long id);

    /**
     *
     * @param customerId
     * @param page
     * @param size
     * @return : it returns the list of orders of given customerId
     */
    Page<OrderResponseDto> getOrdersByCustomerId(Long customerId, int page, int size);
    /**
     *
     * @param cartCheckOutRequest
     */
    void createOrderFromCart(CartCheckOutRequest cartCheckOutRequest);

    /**
     *
     * @param buyNowRequest
     */
    void createOrderFromBuyNow(BuyNowRequest buyNowRequest);

    /**
     *
     * @param orderId
     * @param orderStatus
     */
    void makeOrderTransition(Long orderId, OrderStatus orderStatus);
}
