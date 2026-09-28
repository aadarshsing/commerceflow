package com.shipment.service.client;

import com.shipment.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.commerceflow.dto.notification.CreateNotificationDto;
import org.commerceflow.dto.notification.NotificationResponseDto;
import org.commerceflow.dto.notification.UpdateNotificationDto;
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
