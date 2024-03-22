package com.heygongc.notification.application;

import com.heygongc.common.DatabaseCleaner;
import com.heygongc.device.application.DeviceSensitivityEnum;
import com.heygongc.device.domain.Device;
import com.heygongc.device.domain.DeviceRepository;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.NotificationRepository;
import com.heygongc.notification.domain.type.NotificationType;
import com.heygongc.notification.presentation.request.AddNotificationRequest;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import com.heygongc.user.domain.type.SnsType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
class NotificationServiceTest {

    @Autowired
    private DatabaseCleaner cleaner;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;


    @AfterEach
    void tearDown() {
        cleaner.execute();
    }

    @Test
    @DisplayName("알림을 추가한다")
    public void addNotification() {
        // given
        User user = 사용자_등록();
        Device device = 디바이스_등록(user);

        // when
        notificationService.addNotification(user, addNotificationRequest(device));

        // then
        Assertions.assertThat(notificationRepository.findAllNotificationByUserSeq(user.getSeq())).isNotNull();
    }

    @Test
    @DisplayName("사용자의 알림 목록을 조회한다")
    public void getAllNotifications() {
        // given
        User user = 사용자_등록();
        Device device = 디바이스_등록(user);
        Notification notification = 알림_등록(user, device);

        // when
        List<Notification> notifications = notificationService.getAllNotifications(user.getSeq());

        // then
        Assertions.assertThat(notifications).isNotNull();
        Assertions.assertThat(notifications.size()).isGreaterThan(0);
        Assertions.assertThat(notifications.get(0).getNotiSeq()).isNotNull();
    }

    private AddNotificationRequest addNotificationRequest(Device device) {
        return new AddNotificationRequest(NotificationType.SOUND.name(), device.getDeviceSeq());
    }

    private User 사용자_등록() {
        return userRepository.save(User.createUser()
                .deviceId("1111")
                .snsId("123456789")
                .snsType(SnsType.GOOGLE)
                .deviceOs("AOS")
                .email("test@test.com")
                .alarm(true)
                .ads(true)
                .build());
    }

    private Device 디바이스_등록(User user) {
        return deviceRepository.save(Device.builder()
                .type("iPhone 15 Pro")
                .name("testDeviceName")
                .soundMode(false)
                .sensitivity(DeviceSensitivityEnum.MEDIUM)
                .soundActive(false)
                .streamActive(false)
                .frontCamera(false)
                .user(user)
                .build());
    }

    private Notification 알림_등록(User user, Device device) {
        return notificationRepository.save(Notification.createNotification()
                .type(NotificationType.SOUND)
                .user(user)
                .device(device)
                .build());
    }
}