package com.cart.controller;


import com.cart.dto.cart.CartResponseDto;
import com.cart.dto.cart.CreateCartDto;
import com.cart.dto.cart.ResponseDto;
import com.cart.service.IcartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CartController {

    @Autowired
    private IcartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<ResponseDto> createCart(@Valid @RequestBody CreateCartDto createCartDto){
        cartService.createCart(createCartDto);
        return new ResponseEntity<>(
                new ResponseDto(
                        HttpStatus.CREATED.toString(),
                        "cart is created Successfully"
                ),
                HttpStatus.CREATED
        );

    }
    @GetMapping("/carts/customer/{customerId}")
    public ResponseEntity<CartResponseDto> getCart(
            @NotNull(message = "customerId cannot be null")
            @PathVariable Long customerId){
        CartResponseDto cartResponseDto = cartService.getCart(customerId);
        return ResponseEntity.ok(
                cartResponseDto
        );
    }
    @DeleteMapping("carts/{cartId}")
    public ResponseEntity<ResponseDto> deleteCartItems(
            @NotNull(message = "CartId cannot be null")
            @PathVariable Long cartId
    ){
        ResponseDto responseDto = cartService.deleteCart(cartId);
        return ResponseEntity.ok(responseDto);
    }
}
