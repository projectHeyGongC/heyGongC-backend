package com.heygongc.notification.domain;

import com.heygongc.notification.application.NotificationTypeEnum;
import com.heygongc.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomNotificationRepository {
    long deleteOldNotifications(LocalDateTime dateTime);

    Notification findNotificationBySeqAndUser(Long eventSeq, User user);

    List<Notification> findByUserSeqAndType(Long userSeq, NotificationTypeEnum type);

}

