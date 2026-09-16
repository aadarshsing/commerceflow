package com.notification.dto;

import com.notification.entity.enums.NotificationStatus;
import com.notification.entity.enums.NotificationType;

public record UpdateNotificationDto (
        NotificationType notificationType,
        NotificationStatus notificationStatus
){
}
