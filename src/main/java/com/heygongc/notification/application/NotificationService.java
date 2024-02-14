package com.heygongc.notification.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.device.exception.DeviceNotFoundException;
import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.notification.domain.Notification;
import com.heygongc.notification.domain.NotificationRepository;
import com.heygongc.notification.exception.NotificationNotFoundException;
import com.heygongc.notification.presentation.request.NotificationInfoRequest;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class NotificationService  {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    public NotificationService( NotificationRepository notificationRepository, UserRepository userRepository, DeviceRepository deviceRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    public Notification getNotification(Long eventSeq, User user) {

        Notification notification = notificationRepository.findNotificationBySeqAndUser(eventSeq, user);
        if (notification == null) {
            throw new NotificationNotFoundException();
        }
        return notification;
    }

    public List<Notification> getAllNotifications(Long userSeq, NotificationTypeEnum type) {

        return notificationRepository.findByUserSeqAndType(userSeq,type);
    }

    public Notification addNotification(User user, Long deviceSeq, NotificationInfoRequest request) {

        Device device = deviceRepository.findDeviceBySeqAndUser(deviceSeq, user);
        if (device == null) {
            throw new DeviceNotFoundException();
        }

        Notification notification = Notification.builder()
                .typeEnum(request.type())
                .content(request.content())
                .readStatus(false)
                .device(device)
                .user(user)
                .build();

        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification updateReadStatus(Long eventSeq, User user) {
        Notification notification = notificationRepository.findNotificationBySeqAndUser(eventSeq, user);

        if (notification != null) {
            notification.setReadStatus(true);
            return notification;
        } else {
            throw new ForbiddenException();
        }

    }

    @Transactional
    public Boolean deleteOldNotifications(Long userSeq) {

        // Step 1: Delete notifications older than 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteOldNotifications(thirtyDaysAgo);

        // Step 2: Ensure only 100 notifications per type remain
        for (NotificationTypeEnum type : NotificationTypeEnum.values()) {
            List<Notification> notifications = notificationRepository.findTop101ByTypeEnumOrderByCreatedAtDesc(type);
            if (notifications.size() > 100) {
                List<Notification> toDelete = notifications.subList(100, notifications.size());
                notificationRepository.deleteAll(toDelete);
            }
        }

        return true;
    }
}
