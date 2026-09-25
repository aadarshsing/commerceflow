package com.order.client;

import com.order.dto.ResponseDto;
import com.order.client.fallback.NotificationFallBack;
import jakarta.validation.Valid;
import org.commerceflow.dto.notification.CreateNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification",fallback = NotificationFallBack.class)
public interface NotificationFeignClient {

    @PostMapping("api/notifications")
    ResponseEntity<ResponseDto> createNotification(@Valid @RequestBody CreateNotificationDto createNotificationDto);

}
