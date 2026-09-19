package com.order.dto.notification;

import com.order.entity.enums.notification.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateNotificationDto(
        @NotNull(message = "customerId cannot be null")
        Long customerId,
        @NotNull(message = "Notification type cannot be null")
        NotificationType type,
        String channel,
        String subject,
        @NotNull(message = "Message cannot be null")
        String message,
        @NotBlank(message = "ReferenceId cannot be null blank or empty")
        String referenceId
) {
}
