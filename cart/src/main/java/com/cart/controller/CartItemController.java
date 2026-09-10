package com.cart.controller;


import com.cart.dto.CartResponseDto;
import com.cart.dto.CreateCartItemDto;
import com.cart.dto.ResponseDto;
import com.cart.dto.UpdateCartItemDto;
import com.cart.service.ICartItemService;
import com.cart.service.IcartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Validated
public class CartItemController {

    @Autowired
    ICartItemService cartItemService;
    @Autowired
    IcartService cartService;

    @PostMapping("/carts/{cartId}/items")
    ResponseEntity<ResponseDto> createCartItem(
            @NotNull(message = "cartId cannot be null")
            @PathVariable Long cartId, @Valid @RequestBody CreateCartItemDto createCartItemDto){
        cartItemService.createCartItem(cartId,createCartItemDto);

        return new ResponseEntity<>(
                new ResponseDto(
                        HttpStatus.CREATED.toString(),
                        "cartItem is created Successfully"
                ),
                HttpStatus.CREATED
        );
    }
    @PostMapping("/carts/{cartId}/bulk/items")
    ResponseEntity<ResponseDto> createCartItemsInBulk(
            @NotNull(message = "cartId cannot be null")
            @PathVariable Long cartId, @Valid @RequestBody List<CreateCartItemDto> createCartItemDto
    ){
        cartItemService.createBulkCartItem(cartId,createCartItemDto);
        return new ResponseEntity<>(
                new ResponseDto(
                        HttpStatus.CREATED.toString(),
                        "cartItems are created Successfully"
                ),
                HttpStatus.CREATED
        );
    }
    @GetMapping("/carts/{cartId}")
    ResponseEntity<CartResponseDto> getCart(
            @NotNull(message = "cartId cannot be null")
            @PathVariable Long cartId){
        CartResponseDto cartItems = cartItemService.getCart(cartId);
        return ResponseEntity.ok(cartItems);

    }

    @PutMapping("/carts/{cartId}/items/{itemsId}")
    ResponseEntity<CartResponseDto> updateCartItem(
            @NotNull(message = "cartId cannot be null")
            @PathVariable Long cartId,
            @NotNull(message = "itemsId cannot be null")
            @PathVariable Long itemsId,@Valid @RequestBody UpdateCartItemDto cartItem){

        CartResponseDto cartResponseDto = cartItemService.updateCartItem(cartId,itemsId,cartItem);
        return new ResponseEntity<>(
                cartResponseDto,
                HttpStatus.OK
        );

    }

    @DeleteMapping("/carts/{cartId}/items/{itemId}")
    ResponseEntity<ResponseDto> deleteCartItem(
            @NotNull(message = "cartId cannot be null")
            @PathVariable Long cartId,
            @NotNull(message = "cartId cannot be null")
            @PathVariable Long itemId){

        cartItemService.deleteCartItem(cartId,itemId);
        return ResponseEntity.ok(
                new ResponseDto(
                        HttpStatus.OK.toString(),
                        "cartItem is deleted Successfully"
                )
        );


    }
}
