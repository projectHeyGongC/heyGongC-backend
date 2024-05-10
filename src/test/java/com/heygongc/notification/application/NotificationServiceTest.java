package com.heygongc.notification.application;

import com.heygongc.common.ServiceTest;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.NotificationRepository;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    User 구글테스트계정;

    @BeforeEach
    void set구글테스트계정() {
        구글테스트계정 = saveGoogleUser();
        userRepository.save(구글테스트계정);
    }

    @Test
    @DisplayName("사용자의 알림 목록을 조회한다")
    public void 알림목록조회() {
        // given
//        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스 = saveDevice(구글테스트계정);
        Notification 알림 = saveNotification(구글테스트계정, 디바이스);

        // when
        List<Notification> notifications = notificationService.getNotifications(구글테스트계정);

        // then
        Assertions.assertThat(notifications).isNotNull();
        Assertions.assertThat(notifications.size()).isGreaterThan(0);
        Assertions.assertThat(notifications.get(0).getNotiSeq()).isNotNull();
    }
}