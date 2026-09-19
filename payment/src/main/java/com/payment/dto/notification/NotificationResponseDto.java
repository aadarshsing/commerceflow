package com.payment.dto.notification;


import com.payment.entity.enums.notification.NotificationStatus;
import com.payment.entity.enums.notification.NotificationType;

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
