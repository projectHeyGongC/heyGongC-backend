package com.heygongc.notification.application;

import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.notification.NotificationRepository;
import com.heygongc.user.domain.entity.User;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotifications(User user) {

        return notificationRepository.getNotifications(user.getUserSeq());
    }

    public List<Notification> getNotifications(User user, String requestAt) throws ParseException {

        return notificationRepository.getNotifications(user.getUserSeq(), requestAt);
    }

    public List<Notification> getNotifications(User user, String deviceId, String requestAt) throws ParseException {

        return notificationRepository.getNotifications(user.getUserSeq(), deviceId, requestAt);
    }
}
