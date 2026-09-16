package com.order.service.client;

import com.order.dto.notification.CreateNotificationDto;
import com.order.dto.notification.NotificationResponseDto;
import com.order.dto.order.ResponseDto;
import com.order.dto.notification.UpdateNotificationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification")
public interface NotificationFeignClient {

    @PostMapping("api/notifications")
    ResponseEntity<ResponseDto> createNotification(@Valid @RequestBody CreateNotificationDto createNotificationDto);

    @PatchMapping("api/notifications/{notificationId}/status")
    ResponseEntity<NotificationResponseDto> updateNotification(
            @PathVariable
            @NotNull(message = "Notification id cannot be null")
            Long notificationId
            , @RequestBody @Valid UpdateNotificationDto updateNotificationDto);


}
