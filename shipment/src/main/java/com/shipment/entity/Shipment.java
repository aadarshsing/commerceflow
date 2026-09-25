package com.shipment.entity;

import com.shipment.entity.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "shipment")
@Getter @Setter
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "order_id",
            nullable = false,
            unique = true
    )
    private Long orderId;
    @Column(
            nullable = false
    )
    private Long customerId;
    @Column(
            nullable = false,
            unique = true
    )
    private String trackingNumber;
    private String carrier;
    @Column(nullable = false,unique = true)
    private String idempotencyKey;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ShippingAddress shippingAddress;
    private Instant estimatedDeliveryDate;
    private Instant shippedAt;
    private Instant deliveredAt;

}
