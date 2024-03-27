package com.heygongc.notification.application;

import com.heygongc.common.ServiceTest;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.global.type.OsType;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.NotificationRepository;
import com.heygongc.notification.domain.type.NotificationType;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import com.heygongc.user.domain.type.SnsType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
class NotificationServiceTest extends ServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자의 알림 목록을 조회한다")
    public void getAllNotifications() {
        // given
        User user = 사용자_등록();
        Device device = 디바이스_등록(user);
        Notification notification = 알림_등록(user, device);

        // when
        List<Notification> notifications = notificationService.getAllNotifications(user.getUserSeq());

        // then
        Assertions.assertThat(notifications).isNotNull();
        Assertions.assertThat(notifications.size()).isGreaterThan(0);
        Assertions.assertThat(notifications.get(0).getNotiSeq()).isNotNull();
    }

    private User 사용자_등록() {
        return userRepository.save(User.createUser()
                .deviceId("1111")
                .snsId("123456789")
                .snsType(SnsType.GOOGLE)
                .deviceOs(OsType.AOS)
                .email("test@test.com")
                .alarm(true)
                .ads(true)
                .build());
    }

    private Device 디바이스_등록(User user) {

        Device device = Device.createDevice()
                .deviceId("123123")
                .modelName("IPHONE14")
                .deviceOs(OsType.AOS)
                .fcmToken("4712478v")
                .build();

        device.changeDeviceName("거실");
        device.setDeviceOwner(user.getUserSeq());
        device.pairDevice();


        return deviceRepository.save(device);
    }

    private Notification 알림_등록(User user, Device device) {
        return notificationRepository.save(Notification.createNotification()
                .type(NotificationType.SOUND)
                .user(user)
                .device(device)
                .build());
    }
}