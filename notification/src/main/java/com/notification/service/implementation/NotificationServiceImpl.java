package com.notification.service.implementation;

import com.notification.dto.CreateNotificationDto;
import com.notification.dto.NotificationResponseDto;
import com.notification.dto.ResponseDto;
import com.notification.dto.UpdateNotificationDto;
import com.notification.entity.Notification;
import com.notification.exception.ResourceNotFoundException;
import com.notification.mapper.NotificationMapper;
import com.notification.repository.NotificationRepository;
import com.notification.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public ResponseDto createNotification(CreateNotificationDto createNotificationDto) {
        Notification notification = NotificationMapper.createNotificationDtoToNotificationMapper(new Notification(),createNotificationDto);
        notificationRepository.save(notification);
        return new ResponseDto(
                HttpStatus.CREATED.toString(),
                notification.getId().toString()
        );
    }

    @Override
    public NotificationResponseDto getNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                ()-> new ResourceNotFoundException("Notification","NotificationId",notificationId.toString())
        );
        return NotificationMapper.notificationToNotificationResponseDtoMapper(notification);
    }

    @Override
    public List<NotificationResponseDto> getNotificationByCustomerId(Long customerId) {
        List<Notification> notification = notificationRepository.findAllByCustomerId(customerId).orElseThrow(
                ()-> new ResourceNotFoundException("Notification","NotificationId",customerId.toString())
        );
        return notification.stream().map(NotificationMapper::notificationToNotificationResponseDtoMapper).toList();
    }

    @Override
    public NotificationResponseDto updateNotification(Long notificationId,UpdateNotificationDto updateNotificationDto) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                ()-> new ResourceNotFoundException("Notification","NotificationId",notificationId.toString())
        );
        notification = NotificationMapper.updateNotificationDtoToNotificationMapper(notification,updateNotificationDto);
        notificationRepository.save(notification);
        return NotificationMapper.notificationToNotificationResponseDtoMapper(notification);
    }
}
