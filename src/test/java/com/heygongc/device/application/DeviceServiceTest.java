package com.heygongc.device.application;

import com.heygongc.common.ServiceTest;
import com.heygongc.device.application.device.DeviceService;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.domain.type.CameraModeType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.device.presentation.request.device.DeviceInfoRequest;
import com.heygongc.user.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.heygongc.device.setup.DeviceSetup.saveDevice;
import static com.heygongc.user.setup.UserSetup.saveGoogleUser;
public class DeviceServiceTest extends ServiceTest {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    @DisplayName("디바이스 정보 가져오기")
    void getDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);

        // when
        Device 저장된디바이스 = deviceService.getDevice(내디바이스.getDeviceId(), 구글테스트계정);

        // then
        Assertions.assertThat(저장된디바이스.getUserSeq()).isEqualTo(구글테스트계정.getUserSeq());
    }

    @Test
    @DisplayName("디바이스 목록 가져오기")
    void getAllDevices() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);

        // when
        List<Device> 디바이스목록 = deviceService.getDevices(구글테스트계정);

        // then
        Assertions.assertThat(디바이스목록).isNotNull();
        Assertions.assertThat(디바이스목록.size()).isGreaterThan(0);
        Assertions.assertThat(디바이스목록.get(0).getDeviceSeq()).isEqualTo(내디바이스.getDeviceSeq());
        Assertions.assertThat(디바이스목록.get(0).getUserSeq()).isEqualTo(구글테스트계정.getUserSeq());
    }

    @Test
    @DisplayName("디바이스 연동하기")
    void SubscribeDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice();
        String 새로운디바이스명 = "newDeviceName";

        // when
        deviceService.subscribeDevice(
                deviceInfoRequest(내디바이스.getDeviceId(), 새로운디바이스명),
                구글테스트계정);

        // then
        내디바이스 = deviceRepository.findMyDevice(내디바이스.getDeviceId(), 구글테스트계정).get();

        Assertions.assertThat(내디바이스.getDeviceName()).isEqualTo(새로운디바이스명);
        Assertions.assertThat(내디바이스.getUserSeq()).isEqualTo(구글테스트계정.getUserSeq());
        Assertions.assertThat(내디바이스.isConnected()).isTrue();
    }

    @Test
    @DisplayName("디바이스 이름 수정하기")
    void updateDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);
        String 새로운디바이스명 = "newDeviceName";

        // when
        deviceService.updateDevice(내디바이스.getDeviceId(), 새로운디바이스명, 구글테스트계정);

        // then
        내디바이스 = deviceRepository.findMyDevice(내디바이스.getDeviceId(), 구글테스트계정).get();

        Assertions.assertThat(내디바이스.getDeviceName()).isEqualTo(새로운디바이스명);
    }

    @Test
    @DisplayName("디바이스 목록 가져오기2")
    void getDevices() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);

        List<String> 기기아이디목록 = Arrays.asList(내디바이스.getDeviceId());

        // when
        List<Device> 디바이스목록 = deviceService.getDevices(기기아이디목록, 구글테스트계정);

        // then
        Assertions.assertThat(디바이스목록).isNotNull();
        Assertions.assertThat(디바이스목록.size()).isOne();
        Assertions.assertThat(디바이스목록.get(0).getDeviceId()).isEqualTo(내디바이스.getDeviceId());
    }

    @Test
    @DisplayName("디바이스 연동 해제하기")
    void disconnectDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);

        List<Device> 디바이스목록 = Arrays.asList(내디바이스);

        // when
        deviceService.disconnectDevices(디바이스목록);

        // then
        Assertions.assertThat(내디바이스.getUserSeq()).isNull();
        Assertions.assertThat(내디바이스.getFcmToken()).isNull();
        Assertions.assertThat(내디바이스.isConnected()).isFalse();
    }

    @Test
    @DisplayName("디바이스 세팅 변경하기")
    void changeDeviceSetting() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device before디바이스 = saveDevice(구글테스트계정);
        String before민감도 = SensitivityType.MEDIUM.toString();
        String before카메라모드 = CameraModeType.FRONT.toString();
        String after민감도 = SensitivityType.HIGH.toString();
        String after카메라모드 = CameraModeType.BACK.toString();

        // when
        deviceService.changeDeviceSetting(before디바이스.getDeviceId(), after민감도, after카메라모드, 구글테스트계정);

        // then
        Device after디바이스 = deviceRepository.findMyDevice(before디바이스.getDeviceId(), 구글테스트계정).get();

        Assertions.assertThat(before디바이스.getSensitivity().toString()).isEqualTo(before민감도);
        Assertions.assertThat(before디바이스.getCameraMode().toString()).isEqualTo(before카메라모드);
        Assertions.assertThat(after디바이스.getSensitivity().toString()).isEqualTo(after민감도);
        Assertions.assertThat(after디바이스.getCameraMode().toString()).isEqualTo(after카메라모드);
    }


    private DeviceInfoRequest deviceInfoRequest(String deviceId, String deviceName) {
        return new DeviceInfoRequest(deviceId, deviceName
        );
    }

}
