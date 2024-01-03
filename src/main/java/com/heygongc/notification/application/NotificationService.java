package com.heygongc.notification.application;

import com.heygongc.notification.domain.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotifications(Long userSeq, String type);

    Notification addNotification(Long userSeq, Long deviceSeq, NotificationTypeEnum type, String Content);

    Boolean updateReadStatus(Long eventSeq, Long userSeq);

    Boolean deleteOldNotifications(Long userSeq);


}
