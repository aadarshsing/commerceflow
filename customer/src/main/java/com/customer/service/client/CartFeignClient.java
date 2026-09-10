package com.customer.service.client;

import com.customer.dto.CreateCartDto;
import com.customer.dto.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cart")
public interface CartFeignClient {

    @PostMapping("api/carts")
    public ResponseEntity<ResponseDto> createCart(@Valid @RequestBody CreateCartDto createCartDto);
}
