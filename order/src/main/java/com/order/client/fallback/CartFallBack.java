package com.order.client.fallback;

import com.order.dto.ResponseDto;
import com.order.exception.ServiceUnavailableException;
import com.order.client.CartFeignClient;
import org.commerceflow.dto.cart.CartResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CartFallBack implements CartFeignClient {
    @Override
    public ResponseEntity<CartResponseDto> getCart(Long cartId) {
        throw new ServiceUnavailableException(
                "Cart Service is currently unavailable"
        );
    }

    @Override
    public ResponseEntity<ResponseDto> deleteCartItems(Long cartId) {
        throw new ServiceUnavailableException(
                "Cart Service is currently unavailable"
        );
    }
}
