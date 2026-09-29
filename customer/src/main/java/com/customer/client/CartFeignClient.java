package com.customer.client;

import com.customer.config.RetryConfig;
import com.customer.dto.ResponseDto;
import com.customer.client.fallback.CartFallBack;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.commerceflow.dto.cart.CreateCartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "cart",
        fallback = CartFallBack.class,
        configuration = RetryConfig.class)
public interface CartFeignClient {

    @PostMapping("api/carts")
    ResponseEntity<ResponseDto> createCart(@Valid @RequestBody CreateCartDto createCartDto, @RequestHeader String idempotencyKey);

    @DeleteMapping("api/carts/{customerId}/delete")
    ResponseEntity<ResponseDto> deleteCart(
            @NotNull(message = "CartId cannot be null")
            @PathVariable Long customerId);
}
