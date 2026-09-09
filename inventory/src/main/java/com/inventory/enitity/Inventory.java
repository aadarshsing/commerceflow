package com.inventory.enitity;

import com.inventory.enitity.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_inventory_product",
                columnNames = "product_id"
        )
)
@Getter
@Setter
public class Inventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Long productId;
    @Column(nullable = false)
    private int availableQuantity;
    @Column(nullable = false)
    private int reservedQuantity;
    @Column(nullable = false)
    private int lowStockThreshold;
    @Enumerated(EnumType.STRING)
    private Status status;


}
