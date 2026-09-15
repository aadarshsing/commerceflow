package com.notification.service;

import com.notification.dto.CreateNotificationDto;
import com.notification.dto.NotificationResponseDto;
import com.notification.dto.ResponseDto;
import com.notification.dto.UpdateNotificationDto;

public interface INotificationService {

    /**
     *
     * @param createNotificationDto
     * @return : ResponseDto
     */
    ResponseDto createNotification(CreateNotificationDto createNotificationDto);

    /**
     *
     * @param notificationId
     * @return NotificationResponseDto
     */
    NotificationResponseDto getNotification(Long notificationId);

    /**
     *
     * @param customerId
     * @return NotificationResponseDto
     */
    NotificationResponseDto getNotificationByCustomerId(Long customerId);


    /**
     *
     * @param notificationId
     * @param updateNotificationDto
     * @return
     */
    NotificationResponseDto updateNotification(Long notificationId,UpdateNotificationDto updateNotificationDto);

}
