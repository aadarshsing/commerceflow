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
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "shipping_address_id",
            nullable = false
    )
    private ShippingAddress shippingAddress;
    private Instant estimatedDeliveryDate;
    private Instant shippedAt;
    private Instant deliveredAt;

}
