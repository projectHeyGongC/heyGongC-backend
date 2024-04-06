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
import static com.heygongc.device.setup.DeviceSetup.saveSpecificDevice;
import static com.heygongc.user.setup.UserSetup.saveGoogleUser;
public class DeviceServiceTest extends ServiceTest {

    @Autowired
    private DeviceService deviceService;

    @Test
    @DisplayName("디바이스 정보 가져오기")
    void getDevice() {
        // given
        Device 내디바이스 = saveDevice();
        User 구글테스트계정 = saveGoogleUser();

        // when
        Device 저장된디바이스 = deviceService.getDevice(내디바이스.getDeviceId(), 구글테스트계정);

        // then
        Assertions.assertThat(내디바이스.getUserSeq()).isEqualTo(저장된디바이스.getUserSeq());

    }

    @Test
    @DisplayName("디바이스 목록 가져오기")
    void getAllDevices() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);

        // when
        List<Device> 디바이스목록 = deviceService.getAllDevices(구글테스트계정.getUserSeq());

        // then
        Assertions.assertThat(디바이스목록).isNotNull();
        Assertions.assertThat(디바이스목록.size()).isGreaterThan(0);
        Assertions.assertThat(디바이스목록.get(0).getDeviceSeq()).isEqualTo(내디바이스.getDeviceSeq());

    }

    @Test
    @DisplayName("디바이스 연동하기")
    void SubscribeDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveSpecificDevice(구글테스트계정);


        // when
        deviceService.subscribeDevice(deviceInfoRequest(), 구글테스트계정);

        // then
        Assertions.assertThat(내디바이스.getDeviceName()).isEqualTo("myDevice");
        Assertions.assertThat(내디바이스.getUserSeq()).isEqualTo(구글테스트계정.getUserSeq());
        Assertions.assertThat(내디바이스.isConnected()).isEqualTo(true);


    }

    @Test
    @DisplayName("디바이스 이름 수정하기")
    void updateDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveSpecificDevice(구글테스트계정);

        // when
        deviceService.updateDevice(내디바이스.getDeviceId(), "newName", 구글테스트계정);

        // then
        Assertions.assertThat(내디바이스.getDeviceName()).isEqualTo("newName");

    }

    @Test
    @DisplayName("디바이스 연동 해제하기")
    void disconnectDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);

        List<String> 기기아이디목록 =Arrays.asList(내디바이스.getDeviceId());

        // when
        deviceService.disconnectDevice(기기아이디목록, 구글테스트계정);

        // then
        Assertions.assertThat(내디바이스.isConnected()).isEqualTo(false);

    }

    @Test
    @DisplayName("디바이스 세팅 변경하기")
    void changeDeviceSetting() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);

        // when
        deviceService.changeDeviceSetting(내디바이스.getDeviceId(), "HIGH", "BACK", 구글테스트계정);

        // then
        Assertions.assertThat(내디바이스.getSensitivity()).isEqualTo(SensitivityType.HIGH);
        Assertions.assertThat(내디바이스.getCameraMode()).isEqualTo(CameraModeType.BACK);

    }

    @Test
    @DisplayName("디바이스 명령 내리기")
    void controlDevice() {
        // given
        User 구글테스트계정 = saveGoogleUser();
        Device 내디바이스 = saveDevice(구글테스트계정);

        // when
        deviceService.controlDevice(내디바이스.getDeviceId(), "STREAMON", 구글테스트계정);

        // then
        Assertions.assertThat(내디바이스.isStreamActive()).isEqualTo(true);

    }


    private DeviceInfoRequest deviceInfoRequest() {
        return new DeviceInfoRequest("1234", "myDevice");
    }

}
