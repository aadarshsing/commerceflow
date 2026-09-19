package com.shipment.dto.notification;


import com.shipment.entity.enums.NotificationStatus;
import com.shipment.entity.enums.NotificationType;

public record UpdateNotificationDto(
        NotificationType notificationType,
        NotificationStatus notificationStatus
){
}
