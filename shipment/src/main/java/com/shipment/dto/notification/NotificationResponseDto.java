package com.shipment.dto.notification;


import com.shipment.entity.enums.NotificationStatus;
import com.shipment.entity.enums.NotificationType;

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
