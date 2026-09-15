package com.notification.entity;

import com.commerceflow.order.entity.Order;
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            unique = true
    )
    private Order order;
    @Column(
            nullable = false,
            unique = true
    )
    private String trackingNumber;
    private String carrier;
    private String status;
    private Instant shippedAt;
    private Instant deliveredAt;

}
