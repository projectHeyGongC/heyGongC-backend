package com.heygongc.device.application;

import com.heygongc.common.ServiceTest;
import com.heygongc.device.application.camera.CameraService;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.presentation.request.camera.CameraSubscribeRequest;
import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.repository.NotificationRepository;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.heygongc.device.setup.DeviceSetup.saveDevice;
import static com.heygongc.user.setup.UserSetup.saveGoogleUser;

@SuppressWarnings("NonAsciiCharacters")
public class CameraServiceTest extends ServiceTest {

    @Autowired
    private CameraService cameraService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("Device 정보를 가지고 User 정보를 조회한다")
    void getUserByDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스 = saveDevice(구글테스트계정);

        // when
        User 저장된계정 = cameraService.getUserByDevice(디바이스);

        // then
        Assertions.assertThat(저장된계정).isNotNull();
        Assertions.assertThat(저장된계정.getSeq()).isEqualTo(구글테스트계정.getSeq());
    }

    @Test
    @DisplayName("Device 정보를 가지고 User 정보를 조회할 때, User 정보가 없으면 null을 리턴한다")
    void getUserByDeviceWithNoData() {
        // given
        Device 디바이스 = saveDevice();

        // when
        User 저장된계정 = cameraService.getUserByDevice(디바이스);

        // then
        Assertions.assertThat(저장된계정).isNull();
    }

    @Test
    @DisplayName("카메라 등록하기 - 등록된 경우 token만 발급한다")
    void subscribeCamera() {
        // given
        saveDevice();

        // when
        String accessToken = cameraService.subscribeCamera(cameraSubscribeRequest());

        // then
        Assertions.assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("카메라 등록하기 - 등록되지 않은 경우 등록 후 token을 발급한다")
    void subscribeCameraWithNoData() {
        // given
        long beforeCount = deviceRepository.count();

        // when
        String accessToken = cameraService.subscribeCamera(cameraSubscribeRequest());

        // then
        long afterCount = deviceRepository.count();

        Assertions.assertThat(accessToken).isNotNull();
        Assertions.assertThat(beforeCount).isZero();
        Assertions.assertThat(afterCount).isOne();
    }

    @Test
    @DisplayName("카메라 기기 상태정보 변경하기 - 배터리와 온도를 변경시킨다")
    void changeCameraDeviceStatus() {
        // given
        Device before디바이스 = saveDevice();
        before디바이스.changeCameraDeviceStatus(80, 20);
        int before배터리 = before디바이스.getBattery();
        int before온도 =  before디바이스.getTemperature();

        // when
        cameraService.changeCameraDeviceStatus(before디바이스, 60, 30);

        // then
        Device after디바이스 = deviceRepository.findByDeviceId(before디바이스.getDeviceId()).get();
        int after배터리 = after디바이스.getBattery();
        int after온도 =  after디바이스.getTemperature();

        Assertions.assertThat(before배터리).isNotEqualTo(after배터리);
        Assertions.assertThat(before온도).isNotEqualTo(after온도);
    }

    @Test
    @DisplayName("카메라 연결 상태 조회 - 연결된 경우 true를 리턴한다")
    void isConnectedWithConnected() {
        // given
        Device 디바이스 = saveDevice();
        디바이스.connectDevice();

        // when
        boolean isConnected = cameraService.isConnected(디바이스);

        // then
        Assertions.assertThat(isConnected).isTrue();
    }

    @Test
    @DisplayName("카메라 연결 상태 조회 - 연결되지 않은 경우 false를 리턴한다")
    void isConnectedWithDisConnected() {
        // given
        Device 디바이스 = saveDevice();
        디바이스.disConnectDevice();

        // when
        boolean isConnected = cameraService.isConnected(디바이스);

        // then
        Assertions.assertThat(isConnected).isFalse();
    }

    @Test
    @DisplayName("카메라 소리감지 발생 시 알림에 등록한다")
    void alertSoundAlarm() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 디바이스 = saveDevice(구글테스트계정);
        List<Notification> before알림 = notificationRepository.findAllNotificationByUserSeq(구글테스트계정.getSeq());

        // when
        cameraService.alertSoundAlarm(디바이스, 구글테스트계정);

        // then
        List<Notification> after알림 = notificationRepository.findAllNotificationByUserSeq(구글테스트계정.getSeq());

        Assertions.assertThat(before알림).isEmpty();
        Assertions.assertThat(after알림).isNotEmpty();
        Assertions.assertThat(after알림.get(0).getDevice().getDeviceSeq()).isEqualTo(디바이스.getDeviceSeq());
    }

    @Test
    @DisplayName("미가입한 사용자가 카메라 소리감지 발생 시 에러가 발생한다")
    void alertSoundAlarmWithIsUnRegistered() {
        // given
        Device 디바이스 = saveDevice();

        // then
        Assertions.assertThatThrownBy(() -> cameraService.alertSoundAlarm(디바이스, User.createUser().build()))
                .isInstanceOf(UserNotFoundException.class);
    }


    private CameraSubscribeRequest cameraSubscribeRequest() {
        return new CameraSubscribeRequest("1111", "SM-S921N", "AOS", "fcmToken");
    }
}
