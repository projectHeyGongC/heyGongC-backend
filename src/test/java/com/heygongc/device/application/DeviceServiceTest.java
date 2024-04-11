package com.heygongc.device.application;

import com.heygongc.common.ServiceTest;
import com.heygongc.device.application.device.DevicePushService;
import com.heygongc.device.application.device.DeviceService;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.device.domain.type.CameraModeType;
import com.heygongc.device.domain.type.ControlType;
import com.heygongc.device.domain.type.SensitivityType;
import com.heygongc.device.presentation.request.device.DeviceSubscribeRequest;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.heygongc.device.setup.DeviceSetup.saveDevice;
import static com.heygongc.user.setup.UserSetup.saveGoogleUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class DeviceServiceTest extends ServiceTest {

    @Autowired
    private DeviceService deviceService;
    @MockBean
    private DevicePushService devicePushService;
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    User 구글테스트계정;

    @BeforeEach
    void set구글테스트계정() {
        구글테스트계정 = saveGoogleUser();
        userRepository.save(구글테스트계정);
    }

    @Test
    @DisplayName("디바이스 정보 가져오기")
    void getDevice() {
        // given
//        User 구글테스트계정 = saveGoogleUser();
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
//        User 구글테스트계정 = saveGoogleUser();
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
    void SubscribeDevice() throws Exception {
        // given
//        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice();
        String 새로운디바이스명 = "newDeviceName";
        DeviceSubscribeRequest request = deviceSubscribeRequest(내디바이스.getDeviceId(), 새로운디바이스명);
        doNothing().when(devicePushService).hideQRCode(any());

        // when
        deviceService.subscribeDevice(request.deviceId(), request.deviceName(), 구글테스트계정);

        // then
        verify(devicePushService).hideQRCode(구글테스트계정.getFcmToken());

        내디바이스 = deviceRepository.findMyDevice(내디바이스.getDeviceId(), 구글테스트계정).get();
        Assertions.assertThat(내디바이스.getDeviceName()).isEqualTo(새로운디바이스명);
        Assertions.assertThat(내디바이스.getUserSeq()).isEqualTo(구글테스트계정.getUserSeq());
        Assertions.assertThat(내디바이스.isConnected()).isTrue();
    }

    @Test
    @DisplayName("디바이스 이름 수정하기")
    void changeDeviceName() {
        // given
//        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);
        String 새로운디바이스명 = "newDeviceName";

        // when
        deviceService.changeDeviceName(내디바이스.getDeviceId(), 새로운디바이스명, 구글테스트계정);

        // then
        내디바이스 = deviceRepository.findMyDevice(내디바이스.getDeviceId(), 구글테스트계정).get();

        Assertions.assertThat(내디바이스.getDeviceName()).isEqualTo(새로운디바이스명);
    }

    @Test
    @DisplayName("디바이스 목록 가져오기2")
    void getDevices() {
        // given
//        User 구글테스트계정 = saveGoogleUser();
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
    void disconnectDevice() throws Exception {
        // given
//        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);

        List<String> 디바이스ID목록 = Arrays.asList(내디바이스.getDeviceId());
        List<String> 토큰목록 = Arrays.asList(내디바이스.getFcmToken());
        doNothing().when(devicePushService).showQRCode(any());

        // when
        deviceService.disconnectDevices(디바이스ID목록, 구글테스트계정);

        // then
        verify(devicePushService).showQRCode(토큰목록);

        내디바이스 = deviceRepository.findByDeviceId(내디바이스.getDeviceId()).get();
        Assertions.assertThat(내디바이스.getUserSeq()).isNull();
        Assertions.assertThat(내디바이스.getFcmToken()).isNull();
        Assertions.assertThat(내디바이스.isConnected()).isFalse();
    }

    @Test
    @DisplayName("디바이스 세팅 변경하기")
    void changeDeviceSetting() {
        // given
//        User 구글테스트계정 = saveGoogleUser();
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

    @Test
    @DisplayName("디바이스 제어하기")
    void controlDevice() throws Exception {
        // given
//        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);
        List<ControlType> 컨트롤목록 = new ArrayList<>();
        컨트롤목록.add(ControlType.SOUNDON);
        컨트롤목록.add(ControlType.SOUNDOFF);
        컨트롤목록.add(ControlType.STREAMON);
        컨트롤목록.add(ControlType.STREAMOFF);

        doNothing().when(devicePushService).controlDevice(any(), any());

        for (ControlType type : 컨트롤목록) {
            // when
            deviceService.controlDevice(내디바이스.getDeviceId(), 구글테스트계정, type.toString());

            // then
            verify(devicePushService).controlDevice(type.toString(), 내디바이스.getFcmToken());

            내디바이스 = deviceRepository.findMyDevice(내디바이스.getDeviceId(), 구글테스트계정).get();

            switch (type) {
                case SOUNDON:
                    Assertions.assertThat(내디바이스.isSoundMode()).isTrue();
                    break;
                case SOUNDOFF:
                    Assertions.assertThat(내디바이스.isSoundMode()).isFalse();
                    break;
                case STREAMON:
                    Assertions.assertThat(내디바이스.isStreamActive()).isTrue();
                    break;
                case STREAMOFF:
                    Assertions.assertThat(내디바이스.isStreamActive()).isFalse();
                    break;
                default:
                    Assertions.assertThat(true).isFalse();
            }
        }
    }

    private DeviceSubscribeRequest deviceSubscribeRequest(String deviceId, String deviceName) {
        return new DeviceSubscribeRequest(deviceId, deviceName);
    }
}
