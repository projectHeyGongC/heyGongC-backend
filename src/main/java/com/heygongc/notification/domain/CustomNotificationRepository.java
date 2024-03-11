package com.heygongc.notification.domain;

import com.heygongc.notification.application.NotificationTypeEnum;
import com.heygongc.user.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface CustomNotificationRepository {

    Optional<Notification> findMyNotification(Long eventSeq, User user);

    List<Notification> findNotificationByType(Long userSeq, NotificationTypeEnum type);

}

