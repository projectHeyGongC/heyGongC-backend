package com.heygongc.notification.domain.repository.notification;

import com.heygongc.notification.domain.entity.Notification;

import java.text.ParseException;
import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    List<Notification> getNotifications(Long userSeq);
    List<Notification> getNotifications(Long userSeq, String requestAt) throws ParseException;
    List<Notification> getNotifications(Long userSeq, String deviceId, String requestAt) throws ParseException;
}
