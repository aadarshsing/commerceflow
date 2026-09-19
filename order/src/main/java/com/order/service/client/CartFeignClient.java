package com.order.service.client;

import com.order.dto.order.ResponseDto;
import jakarta.validation.constraints.NotNull;
import org.commerceflow.dto.cart.CartResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart")
public interface CartFeignClient {

    @GetMapping("api/carts/{cartId}")
    ResponseEntity<CartResponseDto> getCart(
            @NotNull(message = "cartId cannot be null")
            @PathVariable Long cartId);
    @DeleteMapping("api/carts/{cartId}")
    public ResponseEntity<ResponseDto> deleteCartItems(
            @NotNull(message = "CartId cannot be null")
            @PathVariable Long cartId
    );
}
