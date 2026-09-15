package com.notification.mapper;

import com.notification.dto.CreateNotificationDto;
import com.notification.dto.NotificationResponseDto;
import com.notification.dto.UpdateNotificationDto;
import com.notification.entity.Notification;

public class NotificationMapper {

    public static Notification createNotificationDtoToNotificationMapper(Notification notification, CreateNotificationDto createNotificationDto){

        notification.setCustomerId(createNotificationDto.customerId());
        notification.setType(createNotificationDto.type());
        notification.setChannel(createNotificationDto.channel());
        notification.setSubject(createNotificationDto.subject());
        notification.setMessage(createNotificationDto.message());
        notification.setReferenceId(createNotificationDto.referenceId());
        return notification;

    }

    public static NotificationResponseDto notificationToNotificationResponseDtoMapper(Notification notification){
        return new NotificationResponseDto(
                notification.getId(),
                notification.getCustomerId(),
                notification.getType(),
                notification.getChannel(),
                notification.getSubject(),
                notification.getMessage(),
                notification.getReferenceId(),
                notification.getStatus()
        );
    }
    public static Notification updateNotificationDtoToNotificationMapper(Notification notification, UpdateNotificationDto updateNotificationDto){
        notification.setStatus(updateNotificationDto.notificationStatus());
        return notification;
    }

}
