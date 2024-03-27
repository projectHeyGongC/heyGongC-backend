package com.heygongc.device.setup;

import com.heygongc.common.ApplicationContextProvider;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.DeviceRepository;
import com.heygongc.global.type.OsType;
import com.heygongc.user.domain.entity.User;

public class DeviceSetup {

    private static final DeviceRepository deviceRepository = ApplicationContextProvider.getBean(DeviceRepository.class);

    public static Device saveDevice() {

        Device device = Device.createDevice()
                .deviceId("123123")
                .modelName("IPHONE14")
                .deviceOs(OsType.AOS)
                .fcmToken("4712478v")
                .build();

        return deviceRepository.save(device);
    }

    public static Device saveDevice(User user) {

        Device device = Device.createDevice()
                .deviceId("123123")
                .modelName("IPHONE14")
                .deviceOs(OsType.AOS)
                .fcmToken("4712478v")
                .build();

        device.changeDeviceName("거실");
        device.setDeviceOwner(user.getUserSeq());
        device.connectDevice();

        return deviceRepository.save(device);
    }
}
