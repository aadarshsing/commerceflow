package com.order.service.client;

import com.order.dto.CartResponseDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart")
public interface CartFeignClient {

    @GetMapping("api/carts/customer/{customerId}")
    public ResponseEntity<CartResponseDto> getCart(
            @NotNull(message = "customerId cannot be null")
            @PathVariable Long customerId);
}
