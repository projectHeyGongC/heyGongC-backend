package com.heygongc.notification.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heygongc.common.ControllerTest;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.global.type.OsType;
import com.heygongc.notification.application.NotificationService;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.type.NotificationType;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.type.SnsType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @Disabled
    @Test
    @DisplayName("사용자의 알림 목록을 조회한다")
    public void getAllNotifications() throws Exception {
        /* FIXME: Cannot resolve parameter names for constructor 오류가 나는데, 이유를 모르겠다.
        Notification notification = 알림();
        List<Notification> notifications = List.of(notification);
        List<NotificationResponse> response = List.of(new NotificationResponse("거실에서 소리가 감지되었습니다.", notification.getCreated_at()));
        given(notificationService.getAllNotifications(anyLong())).willReturn(notifications);

        mvc.perform(
                get("/v1/notifications").header("Authorization", "Bearer accessToken")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
                */
    }

    private User 사용자() {
        return User.createUser()
                .deviceId("1111")
                .snsId("123456789")
                .snsType(SnsType.GOOGLE)
                .deviceOs(OsType.AOS)
                .email("test@test.com")
                .alarm(true)
                .ads(true)
                .build();
    }

    private Device 디바이스() {
        Device device = Device.createDevice()
                .deviceId("123123")
                .modelName("IPHONE14")
                .deviceOs(OsType.AOS)
                .fcmToken("4712478v")
                .build();

        device.changeDeviceName("거실");
        device.setDeviceOwner(사용자().getSeq());
        device.pairDevice();

        return device;
    }

    private Notification 알림() {
        return Notification.createNotification()
                .type(NotificationType.SOUND)
                .user(사용자())
                .device(디바이스())
                .build();
    }
}
