package com.heygongc.analysis.application;

import com.heygongc.analysis.presentation.response.AnalysisMainResponse;
import com.heygongc.common.ServiceTest;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.user.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.heygongc.device.setup.DeviceSetup.saveDevice;
import static com.heygongc.notification.setup.NotificationSetup.saveNotification;
import static com.heygongc.user.setup.UserSetup.saveGoogleUser;

@SuppressWarnings("NonAsciiCharacters")
public class AnalysisServiceTest extends ServiceTest {

    @Autowired
    private AnalysisService analysisService;

    @Test
    @DisplayName("메인 response 생성 - 동일 디바이스")
    void makeAnalysisMainResponseWithSameDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스 = saveDevice(구글테스트계정);
        int count = 5;
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            notifications.add(saveNotification(구글테스트계정, 디바이스));
        }
        String returnMsg = "오늘 소리가 " + count + "번 감지되었습니다.";

        // when
        List<AnalysisMainResponse.Notifications> response = analysisService.makeAnalysisMainResponse(notifications);

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isNotEmpty();
        Assertions.assertThat(response.get(0).deviceId()).isEqualTo(디바이스.getDeviceId());
        Assertions.assertThat(response.get(0).deviceName()).isEqualTo(디바이스.getDeviceName());
        Assertions.assertThat(response.get(0).contents()).isEqualTo(returnMsg);
    }

    @Test
    @DisplayName("메인 response 생성 - 다른 디바이스")
    void makeAnalysisMainResponseWithDifferentDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스1 = saveDevice(구글테스트계정);
        Device 디바이스2 = saveDevice(구글테스트계정);
        int count1 = 3;
        int count2 = 7;
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < count1; i++) {
            notifications.add(saveNotification(구글테스트계정, 디바이스1));
        }
        for (int i = 0; i < count2; i++) {
            notifications.add(saveNotification(구글테스트계정, 디바이스2));
        }
        String returnMsg1 = "오늘 소리가 " + count1 + "번 감지되었습니다.";
        String returnMsg2 = "오늘 소리가 " + count2 + "번 감지되었습니다.";

        // when
        List<AnalysisMainResponse.Notifications> response = analysisService.makeAnalysisMainResponse(notifications);

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isNotEmpty();
        Assertions.assertThat(response.get(0).deviceId()).isEqualTo(디바이스1.getDeviceId());
        Assertions.assertThat(response.get(0).deviceName()).isEqualTo(디바이스1.getDeviceName());
        Assertions.assertThat(response.get(0).contents()).isEqualTo(returnMsg1);
        Assertions.assertThat(response.get(1).deviceId()).isEqualTo(디바이스2.getDeviceId());
        Assertions.assertThat(response.get(1).deviceName()).isEqualTo(디바이스2.getDeviceName());
        Assertions.assertThat(response.get(1).contents()).isEqualTo(returnMsg2);
    }

    @Test
    @DisplayName("메인 response 생성 - 순서가 섞인 다른 디바이스")
    void makeAnalysisMainResponseWithShuffledDifferentDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스1 = saveDevice(구글테스트계정);
        Device 디바이스2 = saveDevice(구글테스트계정);
        int count1 = 3;
        int count2 = 5;
        List<Notification> notifications = new ArrayList<>();
        notifications.add(saveNotification(구글테스트계정, 디바이스1));
        notifications.add(saveNotification(구글테스트계정, 디바이스2));
        notifications.add(saveNotification(구글테스트계정, 디바이스1));
        notifications.add(saveNotification(구글테스트계정, 디바이스2));
        notifications.add(saveNotification(구글테스트계정, 디바이스2));
        notifications.add(saveNotification(구글테스트계정, 디바이스1));
        notifications.add(saveNotification(구글테스트계정, 디바이스2));
        notifications.add(saveNotification(구글테스트계정, 디바이스2));
        String returnMsg1 = "오늘 소리가 " + count1 + "번 감지되었습니다.";
        String returnMsg2 = "오늘 소리가 " + count2 + "번 감지되었습니다.";

        // when
        List<AnalysisMainResponse.Notifications> response = analysisService.makeAnalysisMainResponse(notifications);

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isNotEmpty();
        Assertions.assertThat(response.get(0).deviceId()).isEqualTo(디바이스1.getDeviceId());
        Assertions.assertThat(response.get(0).deviceName()).isEqualTo(디바이스1.getDeviceName());
        Assertions.assertThat(response.get(0).contents()).isEqualTo(returnMsg1);
        Assertions.assertThat(response.get(1).deviceId()).isEqualTo(디바이스2.getDeviceId());
        Assertions.assertThat(response.get(1).deviceName()).isEqualTo(디바이스2.getDeviceName());
        Assertions.assertThat(response.get(1).contents()).isEqualTo(returnMsg2);
    }
}
