package com.cart.entity;


import com.cart.entity.enums.CartStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false,unique = true)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartStatus cartStatus;

    @OneToMany(mappedBy = "cart" , fetch = FetchType.LAZY)
    private List<CartItem> items = new ArrayList<>();


}
