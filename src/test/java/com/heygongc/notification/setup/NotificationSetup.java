package com.heygongc.notification.setup;

import com.heygongc.common.ApplicationContextProvider;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.NotificationRepository;
import com.heygongc.notification.domain.type.NotificationType;
import com.heygongc.user.domain.entity.User;

public class NotificationSetup {

    private static final NotificationRepository notificationRepository = ApplicationContextProvider.getBean(NotificationRepository.class);

    public static Notification saveNotification() {
        return notificationRepository.save(Notification.createNotification()
                .type(NotificationType.SOUND)
                .build());
    }

    public static Notification saveNotification(User user) {
        return notificationRepository.save(Notification.createNotification()
                .type(NotificationType.SOUND)
                .user(user)
                .build());
    }

    public static Notification saveNotification(Device device) {
        return notificationRepository.save(Notification.createNotification()
                .type(NotificationType.SOUND)
                .device(device)
                .build());
    }

    public static Notification saveNotification(User user, Device device) {
        return notificationRepository.save(Notification.createNotification()
                .type(NotificationType.SOUND)
                .user(user)
                .device(device)
                .build());
    }
}
