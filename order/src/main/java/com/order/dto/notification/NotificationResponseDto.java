package com.order.dto.notification;


import com.order.entity.enums.notification.NotificationStatus;
import com.order.entity.enums.notification.NotificationType;

public record NotificationResponseDto(
        Long id,
        Long customerId,
        NotificationType type,
        String channel,
        String subject,
        String message,
        String referenceId,
        NotificationStatus status
) {
}
