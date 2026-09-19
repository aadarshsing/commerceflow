package org.commerceflow.dto.notification;


import org.commerceflow.enums.notification.NotificationStatus;
import org.commerceflow.enums.notification.NotificationType;

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
