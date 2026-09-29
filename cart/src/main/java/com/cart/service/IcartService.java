package com.cart.service;


import com.cart.dto.cart.CartResponseDto;
import com.cart.dto.cart.CreateCartDto;
import com.cart.dto.cart.ResponseDto;

public interface IcartService {
    /**
     *
     * @param createCartDto
     * @param idempotencyKey
     */
    void createCart(CreateCartDto createCartDto,String idempotencyKey);

    /***
     *
     * @param customerId
     * @return :- it return cart data corresponding to customer Id
     */
    CartResponseDto getCart(Long customerId);

    /**
     *
     * @param customerId
     * @return responseDto whether cart is deleted or not
     */
    ResponseDto deleteCartItems(Long customerId);

    /**
     *
     * @param customerId
     * @return
     */
    ResponseDto deleteCart(Long customerId);
}
