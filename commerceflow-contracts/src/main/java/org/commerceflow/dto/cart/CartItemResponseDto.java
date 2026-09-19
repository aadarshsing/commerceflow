package org.commerceflow.dto.cart;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.commerceflow.dto.catalog.ProductResponseDto;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDto {

    private Long id;
    private ProductResponseDto productResponseDto;
    private int quantity;
    private BigDecimal unitPrice;
}
