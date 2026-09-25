package com.payment.entity;


import com.payment.entity.enums.payment.PaymentMethod;
import com.payment.entity.enums.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "payment"
)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "order_id",
            nullable = false
    )
    private Long orderId;
    @Column(
            name = "customer_id",
            nullable = false
    )
    private Long customerId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Column(nullable = false,unique = true)
    private String idempotencyKey;
    @Column(
            unique = true
    )
    private String transactionReference;
}
