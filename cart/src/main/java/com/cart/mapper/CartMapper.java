package com.cart.mapper;


import com.cart.dto.CartItemResponseDto;
import com.cart.dto.CartResponseDto;
import com.cart.dto.CreateCartDto;
import com.cart.dto.ProductResponseDto;
import com.cart.entity.Cart;
import com.cart.entity.CartItem;
import com.cart.entity.enums.CartStatus;

import java.util.*;

public class CartMapper {


    public  static CartResponseDto CartToDtoMapper(Cart cart){

        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setId(cart.getId());
        cartResponseDto.setCustomerId(cart.getCustomerId());
        return  cartResponseDto;
    }

    public static Cart cartDtoTOCart(CreateCartDto createCartDto, Cart cart){
        cart.setCartStatus(CartStatus.ACTIVE);
        cart.setItems(new ArrayList<>());
        cart.setCustomerId(createCartDto.customerId());
        return  cart;
    }
}
