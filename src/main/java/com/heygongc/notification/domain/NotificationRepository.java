package com.heygongc.notification.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, CustomNotificationRepository {

}
