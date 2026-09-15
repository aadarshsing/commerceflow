package com.notification.repository;

import com.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository  extends JpaRepository<Notification,Long> {


    Optional<Notification> findByCustomerId(Long customerId);
}
