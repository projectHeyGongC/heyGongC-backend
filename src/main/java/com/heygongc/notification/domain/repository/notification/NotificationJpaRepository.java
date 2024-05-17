package com.heygongc.notification.domain.repository.notification;

import com.heygongc.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
