package com.customer.client.fallback;

import com.customer.dto.ResponseDto;
import com.customer.exception.ServiceUnavailableException;
import com.customer.client.CartFeignClient;
import org.commerceflow.dto.cart.CreateCartDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CartFallBack implements CartFeignClient {
    @Override
    public ResponseEntity<ResponseDto> createCart(CreateCartDto createCartDto,String idempotencyKey) {
        throw new ServiceUnavailableException(
                "Cart Service is currently unavailable"
        );
    }

    @Override
    public ResponseEntity<ResponseDto> deleteCart(Long customerId) {
        throw new ServiceUnavailableException(
                "Cart Service is currently unavailable"
        );
    }
}
