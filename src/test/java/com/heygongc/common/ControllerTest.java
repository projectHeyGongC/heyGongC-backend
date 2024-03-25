package com.heygongc.common;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.global.filter.LoginUserArgumentResolver;
import com.heygongc.global.filter.PairDeviceArgumentResolver;
import com.heygongc.global.type.OsType;
import com.heygongc.user.domain.entity.User;
import com.heygongc.user.domain.type.SnsType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
abstract public class ControllerTest {

    @MockBean
    LoginUserArgumentResolver loginUserArgumentResolver;
    @MockBean
    PairDeviceArgumentResolver deviceArgumentResolver;

    // FIXME: BeforeAll로 사용하면 좋을까?
    @BeforeEach
    void setUp() {
        given(loginUserArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(사용자());
        given(deviceArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(디바이스());
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
}
