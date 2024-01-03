package com.heygongc.notification.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.notification.domain.Notification;
import com.heygongc.notification.domain.NotificationRepository;
import com.heygongc.user.domain.User;
import com.heygongc.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public List<Notification> getAllNotifications(Long userSeq, String type) {

        return notificationRepository.findAllByType(type);
    }

    @Override
    public Notification addNotification(Long userSeq, Long deviceSeq, NotificationTypeEnum type, String content) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> new RuntimeException("해당 유저 없음"));
        Device device = deviceRepository.findById(deviceSeq).orElseThrow(() -> new RuntimeException("해당 기기 없음"));

        Notification notification = Notification.builder()
                .typeEnum(type)
                .content(content)
                .read_status(false)
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    public Boolean updateReadStatus(Long eventSeq, Long userSeq) {
        Notification notification = notificationRepository.findById(eventSeq)
                .orElseThrow(() -> new RuntimeException("해당 알림 없음"));

        if (notification != null) {
            notification.setRead_status(true);
        }

        return true;
    }

    @Override
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
