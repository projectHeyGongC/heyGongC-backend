package com.heygongc.notification.domain;

import com.heygongc.notification.application.NotificationTypeEnum;
import com.heygongc.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomNotificationRepository {
    long deleteOldNotifications(LocalDateTime dateTime, Long userSeq);

    Optional<Notification> findMyNotification(Long eventSeq, User user);

    List<Notification> findNotificationByType(Long userSeq, NotificationTypeEnum type);

    long deleteExcessNotifications(Long userSeq, NotificationTypeEnum type, int threshold);

    void cleanUpNotifications(Long userSeq);

}

