package com.cart.service;


import com.cart.dto.CartResponseDto;
import com.cart.dto.CreateCartDto;

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
}
