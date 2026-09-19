package com.order.dto.notification;


import com.order.entity.enums.notification.NotificationStatus;
import com.order.entity.enums.notification.NotificationType;

public record UpdateNotificationDto(
        NotificationType notificationType,
        NotificationStatus notificationStatus
){
}
