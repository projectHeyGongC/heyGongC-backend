package com.heygongc.analysis.application;

import com.heygongc.analysis.presentation.response.AnalysisDetailResponse;
import com.heygongc.analysis.presentation.response.AnalysisMainResponse;
import com.heygongc.common.ServiceTest;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.user.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Test
    @DisplayName("graph 생성")
    void makeAnalysisGraph() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스 = saveDevice(구글테스트계정);
        List<Notification> notifications = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        short totalMinute = (short) ((now.getHour() * 60) + (now.getMinute() / 5 * 5)); // 5분 단위로 나눠서 분 단위로 변경
        int index = totalMinute / 5;
        String hour = String.format("%02d", totalMinute / 60);
        String minute = String.format("%02d", totalMinute % 60);
        String time = hour + ":" + minute;

        notifications.add(saveNotification(구글테스트계정, 디바이스));
        notifications.add(saveNotification(구글테스트계정, 디바이스));
        notifications.add(saveNotification(구글테스트계정, 디바이스));
        notifications.add(saveNotification(구글테스트계정, 디바이스));
        notifications.add(saveNotification(구글테스트계정, 디바이스));

        notifications.forEach(n -> n.setNotificationAt(now));

        // when
        List<AnalysisDetailResponse.Graph> graph = analysisService.makeAnalysisGraph(notifications);

        // then
        Assertions.assertThat(graph).isNotNull();
        Assertions.assertThat(graph).isNotEmpty();
        Assertions.assertThat(graph.get(index).x()).isEqualTo(time);
        Assertions.assertThat(graph.get(index).y()).isEqualTo(5);
    }

    @Test
    @DisplayName("graph 생성 - 자정과 정오")
    void makeAnalysisGraphWithMidnightAndNoon() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스 = saveDevice(구글테스트계정);

        LocalDate today = LocalDate.now();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalTime noon = LocalTime.NOON;
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight); // 00:00
        LocalDateTime todayNoon = LocalDateTime.of(today, noon); // 12:00

        List<Notification> notifications1 = new ArrayList<>();
        notifications1.add(saveNotification(구글테스트계정, 디바이스));
        notifications1.add(saveNotification(구글테스트계정, 디바이스));
        notifications1.add(saveNotification(구글테스트계정, 디바이스));
        notifications1.forEach(n -> n.setNotificationAt(todayMidnight));

        List<Notification> notifications2 = new ArrayList<>();
        notifications2.add(saveNotification(구글테스트계정, 디바이스));
        notifications2.add(saveNotification(구글테스트계정, 디바이스));
        notifications2.forEach(n -> n.setNotificationAt(todayNoon));

        List<Notification> notifications = new ArrayList<>();
        notifications.addAll(notifications1);
        notifications.addAll(notifications2);

        // when
        List<AnalysisDetailResponse.Graph> graph = analysisService.makeAnalysisGraph(notifications);

        // then
        Assertions.assertThat(graph).isNotNull();
        Assertions.assertThat(graph).isNotEmpty();
        Assertions.assertThat(graph.get(0).x()).isEqualTo("00:00");
        Assertions.assertThat(graph.get(0).y()).isEqualTo(3);
        Assertions.assertThat(graph.get(144).x()).isEqualTo("12:00");
        Assertions.assertThat(graph.get(144).y()).isEqualTo(2);
    }

    @Test
    @DisplayName("graph 생성 - 다양한 데이터")
    void makeAnalysisGraphWithVariousData() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스 = saveDevice(구글테스트계정);

        LocalDate today = LocalDate.now();

        List<Notification> notifications = new ArrayList<>();

        // 10개
        for (int i = 0; i < 10; i++) {
            LocalDateTime dt = LocalDateTime.of(today, LocalTime.of(0, i));
            Notification n = saveNotification(구글테스트계정, 디바이스);
            n.setNotificationAt(dt);
            notifications.add(n);
        }

        // 3개
        for (int i = 0; i < 3; i++) {
            LocalDateTime dt = LocalDateTime.of(today, LocalTime.of(1, i));
            Notification n = saveNotification(구글테스트계정, 디바이스);
            n.setNotificationAt(dt);
            notifications.add(n);
        }

        // 3개
        for (int i = 5; i < 8; i++) {
            LocalDateTime dt = LocalDateTime.of(today, LocalTime.of(1, i));
            Notification n = saveNotification(구글테스트계정, 디바이스);
            n.setNotificationAt(dt);
            notifications.add(n);
        }

        // 2개
        for (int i = 10; i < 12; i++) {
            LocalDateTime dt = LocalDateTime.of(today, LocalTime.of(1, i));
            Notification n = saveNotification(구글테스트계정, 디바이스);
            n.setNotificationAt(dt);
            notifications.add(n);
        }

        // 2개
        for (int i = 23; i < 24; i++) {
            LocalDateTime dt = LocalDateTime.of(today, LocalTime.of(1, i));
            Notification n = saveNotification(구글테스트계정, 디바이스);
            n.setNotificationAt(dt);
            notifications.add(n);
        }

        // when
        List<AnalysisDetailResponse.Graph> graph = analysisService.makeAnalysisGraph(notifications);

        // then
        Assertions.assertThat(graph).isNotNull();
        Assertions.assertThat(graph).isNotEmpty();
        Assertions.assertThat(graph.get(0).x()).isEqualTo("00:00");
        Assertions.assertThat(graph.get(0).y()).isEqualTo(5);
        Assertions.assertThat(graph.get(1).x()).isEqualTo("00:05");
        Assertions.assertThat(graph.get(1).y()).isEqualTo(5);
        Assertions.assertThat(graph.get(2).x()).isEqualTo("00:10");
        Assertions.assertThat(graph.get(2).y()).isEqualTo(0);

        Assertions.assertThat(graph.get(12).x()).isEqualTo("01:00");
        Assertions.assertThat(graph.get(12).y()).isEqualTo(3);
        Assertions.assertThat(graph.get(13).x()).isEqualTo("01:05");
        Assertions.assertThat(graph.get(13).y()).isEqualTo(3);
        Assertions.assertThat(graph.get(14).x()).isEqualTo("01:10");
        Assertions.assertThat(graph.get(14).y()).isEqualTo(2);
        Assertions.assertThat(graph.get(15).x()).isEqualTo("01:15");
        Assertions.assertThat(graph.get(15).y()).isEqualTo(0);
        Assertions.assertThat(graph.get(16).x()).isEqualTo("01:20");
        Assertions.assertThat(graph.get(16).y()).isEqualTo(1);
    }
}
