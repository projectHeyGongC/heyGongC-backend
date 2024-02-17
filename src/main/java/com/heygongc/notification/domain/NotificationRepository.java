package com.heygongc.notification.domain;

import com.heygongc.notification.application.NotificationTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>, CustomNotificationRepository {

    List<Notification> findAllByType(String type);

    List<Notification> findTop101ByUserAndTypeEnumOrderByCreatedAtDesc(Long userSeq, NotificationTypeEnum typeEnum);



}
