package com.notification.entity;


import com.notification.entity.enums.NotificationStatus;
import com.notification.entity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "notification",
        indexes = @Index(
                name = "idx_notification_customer_id",
                columnList = "customer_id"
        )
)
@Getter @Setter
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id",nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String channel;

    private String subject;

    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String referenceId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

}
