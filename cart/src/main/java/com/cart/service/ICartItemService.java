package com.cart.service;


import com.cart.dto.CartResponseDto;
import com.cart.dto.CreateCartItemDto;
import com.cart.dto.UpdateCartItemDto;

import java.util.List;

public interface ICartItemService {

    /**
     *
     * @param cartId
     * @param createCartItemDto
     */
    void createCartItem(Long cartId, CreateCartItemDto createCartItemDto);

    /**
     *
     * @param cartId
     * @param createCartItemDtoList
     */
    void createBulkCartItem(Long cartId, List<CreateCartItemDto> createCartItemDtoList);
    /**
     *
     * @param cartId
     * @return -- it return the cart items based on cartItemId
     */
    CartResponseDto getCart(Long cartId);

    /**
     *
     * @param cartId
     * @param itemId
     * @param cartItemDto
     * @return -- it returns update cartResponse
     */
    CartResponseDto updateCartItem(Long cartId, Long itemId, UpdateCartItemDto cartItemDto);

    /**
     *
     * @param cartId
     * @param itemId
     */
    void deleteCartItem(Long cartId,Long itemId);

}
