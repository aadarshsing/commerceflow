package org.commerceflow.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.commerceflow.enums.cart.CartStatus;

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
