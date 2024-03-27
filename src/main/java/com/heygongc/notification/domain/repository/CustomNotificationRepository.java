package com.heygongc.notification.domain.repository;

import com.heygongc.notification.domain.entity.Notification;

import java.util.List;

public interface CustomNotificationRepository {

    List<Notification> findAllNotificationByUserSeq(Long userSeq);
}
