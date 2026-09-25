package com.inventory.enitity;

import com.inventory.enitity.enums.InventoryOperation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "inventory_operations",
        uniqueConstraints = {
                @UniqueConstraint(
                name = "uk_inventory_operation_idempotency",
                columnNames = {"idempotency_key"}
            )
        },
        indexes = {
                @Index(
                      name = "idx_inventory_product_id",
                        columnList = "product_id"
                ),
                @Index(
                        name = "idx_inventory_order_id",
                        columnList = "order_id"
                ),


        }
)
@Getter
@Setter
public class InventoryOperations extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String idempotencyKey;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Long orderId;
    @Enumerated(EnumType.STRING)
    private InventoryOperation operationType;


}
