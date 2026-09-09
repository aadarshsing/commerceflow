package com.cart.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_item",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_cart_product",
                columnNames = {"cart_id","product_id"}
        )
)
@Getter
@Setter
public class CartItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id",nullable = false)
    private Cart cart;

    @Column(name = "product_id",nullable = false)
    private Long productId;
    @Column(nullable = false)

    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;


}
