package org.commerceflow.dto.notification;


import org.commerceflow.enums.notification.NotificationStatus;
import org.commerceflow.enums.notification.NotificationType;

public record UpdateNotificationDto(
        NotificationType notificationType,
        NotificationStatus notificationStatus
){
}
