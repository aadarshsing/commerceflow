package com.order.dto.notification;


import com.order.entity.enums.NotificationStatus;
import com.order.entity.enums.NotificationType;

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
