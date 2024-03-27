package com.heygongc.notification.application;

import com.heygongc.common.ServiceTest;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.user.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.heygongc.device.setup.DeviceSetup.saveDevice;
import static com.heygongc.notification.setup.NotificationSetup.saveNotification;
import static com.heygongc.user.setup.UserSetup.saveGoogleUser;

@SuppressWarnings("NonAsciiCharacters")
class NotificationServiceTest extends ServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("사용자의 알림 목록을 조회한다")
    public void 알림목록조회() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스 = saveDevice(구글테스트계정);
        Notification 알림 = saveNotification(구글테스트계정, 디바이스);

        // when
        List<Notification> notifications = notificationService.getAllNotifications(구글테스트계정.getUserSeq());

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