package com.catalog.product.entity;


import com.catalog.category.entity.Category;
import com.catalog.entity.BaseEntity;
import com.catalog.product.entity.enums.ProductStatus;
import com.catalog.seller.entity.Seller;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "product",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_seller_sku",
                        columnNames = {"seller_id", "sku"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_product_seller_id",
                        columnList = "seller_id"
                ),
                @Index(
                        name = "idx_product_category_id",
                        columnList = "category_id"
                )
        }
)
@Getter
@Setter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id",nullable = false)
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String  sku;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
}
