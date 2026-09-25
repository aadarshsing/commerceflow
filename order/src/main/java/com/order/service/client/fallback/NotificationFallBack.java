package com.order.service.client.fallback;

import com.order.dto.ResponseDto;
import com.order.exception.ServiceUnavailableException;
import com.order.service.client.NotificationFeignClient;
import org.commerceflow.dto.notification.CreateNotificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationFallBack implements NotificationFeignClient {
    @Override
    public ResponseEntity<ResponseDto> createNotification(CreateNotificationDto createNotificationDto) {
        throw new ServiceUnavailableException(
                "Notification Service is currently unavailable"
        );
    }
}
