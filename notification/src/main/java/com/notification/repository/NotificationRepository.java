package com.notification.repository;

import com.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository  extends JpaRepository<Notification,Long> {


    Optional<List<Notification>> findAllByCustomerId(Long customerId);
}
