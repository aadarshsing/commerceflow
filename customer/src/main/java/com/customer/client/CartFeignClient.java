package com.customer.client;

import com.customer.config.RetryConfig;
import com.customer.dto.ResponseDto;
import com.customer.client.fallback.CartFallBack;
import jakarta.validation.Valid;
import org.commerceflow.dto.cart.CreateCartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "cart",
        fallback = CartFallBack.class,
        configuration = RetryConfig.class)
public interface CartFeignClient {

    @PostMapping("api/carts")
    ResponseEntity<ResponseDto> createCart(@Valid @RequestBody CreateCartDto createCartDto, @RequestHeader String idempotencyKey);
}
