package com.order.dto.notification;


import com.order.entity.enums.NotificationStatus;
import com.order.entity.enums.NotificationType;

public record UpdateNotificationDto(
        NotificationType notificationType,
        NotificationStatus notificationStatus
){
}
