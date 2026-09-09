package com.cart.mapper;


import com.cart.dto.CartItemResponseDto;
import com.cart.dto.CreateCartItemDto;
import com.cart.dto.ProductResponseDto;
import com.cart.dto.UpdateCartItemDto;
import com.cart.entity.CartItem;

public class CartItemMapper {


    public static CartItemResponseDto cartItemToDtoMapper(CartItem cartItem){

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setId(cartItem.getId());
        cartItemResponseDto.setQuantity(cartItem.getQuantity());
        cartItemResponseDto.setUnitPrice(cartItem.getUnitPrice());
        return cartItemResponseDto;
    }

    public static CartItem cartItemDtoToCartMapper(CartItem cartItem, CreateCartItemDto createCartItemDto){
        cartItem.setQuantity(createCartItemDto.quantity());
        return  cartItem;

    }
    public  static  CartItem updateCartItemDtoToCartMapper(CartItem cartItem, UpdateCartItemDto cartItemDto){
        cartItem.setQuantity(cartItemDto.quantity());
        return cartItem;
    }
}
