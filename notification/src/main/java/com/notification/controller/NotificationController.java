package com.notification.controller;


import com.notification.dto.CreateNotificationDto;
import com.notification.dto.NotificationResponseDto;
import com.notification.dto.ResponseDto;
import com.notification.dto.UpdateNotificationDto;
import com.notification.service.INotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Validated
public class NotificationController {

    @Autowired
    INotificationService notificationService;

    @PostMapping("/notifications")
    ResponseEntity<ResponseDto> createNotification(@Valid @RequestBody CreateNotificationDto createNotificationDto){

        ResponseDto responseDto = notificationService.createNotification(createNotificationDto);
        return new ResponseEntity<>(
                responseDto,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/notifications/{notificationId}")
    ResponseEntity<NotificationResponseDto> getNotification(@PathVariable @NotNull(message = "Notification id cannot be null") Long notificationId){
        NotificationResponseDto notificationResponseDto = notificationService.getNotification(notificationId);
        return ResponseEntity.ok(notificationResponseDto);
    }
    @GetMapping("/customers/{customerId}/notifications")
    ResponseEntity<List<NotificationResponseDto>> getNotificationByCustomerId(@PathVariable @NotNull(message = "Notification id cannot be null") Long customerId){
        List<NotificationResponseDto> notificationResponseDto = notificationService.getNotificationByCustomerId(customerId);
        return ResponseEntity.ok(notificationResponseDto);
    }

    @PatchMapping("/notifications/{notificationId}/status")
    ResponseEntity<NotificationResponseDto> updateNotification(
            @PathVariable
            @NotNull(message = "Notification id cannot be null")
            Long notificationId
            ,@RequestBody @Valid UpdateNotificationDto updateNotificationDto){
        NotificationResponseDto notificationResponseDto = notificationService.updateNotification(notificationId,updateNotificationDto);
        return ResponseEntity.ok(notificationResponseDto);
    }
}
