package com.order.service.client;

import com.order.dto.CartResponseDto;
import com.order.dto.ResponseDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart")
public interface CartFeignClient {

    @GetMapping("api/carts/customer/{customerId}")
    public ResponseEntity<CartResponseDto> getCart(
            @NotNull(message = "customerId cannot be null")
            @PathVariable Long customerId);

    @DeleteMapping("api/carts/{cartId}")
    public ResponseEntity<ResponseDto> deleteCart(
            @NotNull(message = "CartId cannot be null")
            @PathVariable Long cartid
    );
}
