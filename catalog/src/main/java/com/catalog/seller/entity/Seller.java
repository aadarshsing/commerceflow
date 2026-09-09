package com.catalog.seller.entity;



import com.catalog.entity.BaseEntity;
import com.catalog.product.entity.Product;
import com.catalog.seller.entity.enums.SellerStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "seller")
@Getter
@Setter
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  businessName;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private SellerStatus status;

    @OneToMany(mappedBy = "seller")
    private List<Product> products;
}
