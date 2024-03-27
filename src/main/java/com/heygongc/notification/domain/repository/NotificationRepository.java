package com.heygongc.notification.domain.repository;

import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>, CustomNotificationRepository {

    List<Notification> findAllNotificationByUser(User user);

}
