package com.order.dto.cart;

import com.order.entity.enums.cart.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {

    private Long id;
    private Long customerId;
    private List<CartItemResponseDto> items;
    private CartStatus cartStatus;
}
