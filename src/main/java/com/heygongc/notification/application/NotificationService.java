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
        return notificationRepository.findMyNotification(eventSeq, user)
                .orElseThrow(() -> new NotificationNotFoundException("해당 알림이 존재하지 않습니다."));
    }

    public List<Notification> getAllNotifications(Long userSeq, NotificationTypeEnum type) {

        return notificationRepository.findNotificationByType(userSeq,type);
    }

    public Notification addNotification(User user, Long deviceSeq, NotificationInfoRequest request) {

        Device device = deviceRepository.findMyDevice(deviceSeq, user)
                .orElseThrow(DeviceNotFoundException::new); // Optional을 사용한 처리

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
        Notification notification = notificationRepository.findMyNotification(eventSeq, user)
                .orElseThrow(ForbiddenException::new);

        notification.markAsRead();
        return notification;
    }

    @Transactional
    public Boolean deleteOldNotifications(Long userSeq) {
        // Step 1: Delete notifications older than 30 days for a specific user
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteOldNotifications(thirtyDaysAgo, userSeq);

        // Step 2: Ensure only 100 notifications per type remain for the specific user
        for (NotificationTypeEnum type : NotificationTypeEnum.values()) {
            List<Notification> notifications = notificationRepository.findTop101ByUserAndTypeEnumOrderByCreatedAtDesc(userSeq, type);
            if (notifications.size() > 100) {
                List<Notification> toDelete = notifications.subList(100, notifications.size());
                notificationRepository.deleteAll(toDelete);
            }
        }

        return true;
    }
}
