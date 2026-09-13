package com.cart.service;


import com.cart.dto.CartResponseDto;
import com.cart.dto.CreateCartDto;
import com.cart.dto.ResponseDto;

public interface IcartService {
    /**
     *
     * @param createCartDto
     */
    void createCart(CreateCartDto createCartDto);

    /***
     *
     * @param customerId
     * @return :- it return cart data corresponding to customer Id
     */
    CartResponseDto getCart(Long customerId);

    /**
     *
     * @param cartId
     * @return responseDto whether cart is deleted or not
     */
    ResponseDto deleteCart(Long cartId);
}
