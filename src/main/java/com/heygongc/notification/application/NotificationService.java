package com.heygongc.notification.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.NotificationRepository;
import com.heygongc.notification.domain.type.NotificationType;
import com.heygongc.notification.presentation.request.AddNotificationRequest;
import com.heygongc.user.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService  {

    private final NotificationRepository notificationRepository;
    private final DeviceRepository deviceRepository;

    public NotificationService(NotificationRepository notificationRepository, DeviceRepository deviceRepository) {
        this.notificationRepository = notificationRepository;
        this.deviceRepository = deviceRepository;
    }

    public List<Notification> getAllNotifications(Long userSeq) {
        return notificationRepository.findAllNotificationByUserSeq(userSeq);
    }

    @Transactional
    public void addNotification(User user, AddNotificationRequest request) {
        Device device = deviceRepository.findMyDevice(request.deviceSeq(), user)
                .orElseThrow(DeviceNotFoundException::new);

        notificationRepository.save(Notification.createNotification()
                .type(NotificationType.valueOf(request.type()))
                .device(device)
                .user(user)
                .build());
    }
}
