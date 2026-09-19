package com.payment.service.client;


import com.payment.dto.notification.CreateNotificationDto;
import com.payment.dto.payment.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification")
public interface NotificationFeignClient {

    @PostMapping("api/notifications")
    ResponseEntity<ResponseDto> createNotification(@Valid @RequestBody CreateNotificationDto createNotificationDto);

}
