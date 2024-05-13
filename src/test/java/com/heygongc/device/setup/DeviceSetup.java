package com.heygongc.device.setup;

import com.heygongc.common.ApplicationContextProvider;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.repository.device.DeviceRepository;
import com.heygongc.global.type.OsType;
import com.heygongc.user.domain.entity.User;

public class DeviceSetup {

    private static final DeviceRepository deviceRepository = ApplicationContextProvider.getBean(DeviceRepository.class);

    public static Device saveDevice() {

        Device device = Device.createDevice()
                .deviceId("deviceId" + ((int) (Math.random() * 9999) + 1))
                .modelName("IPHONE" + ((int) (Math.random() * 99) + 1))
                .deviceOs(OsType.AOS)
                .fcmToken("4712478v" + ((int) (Math.random() * 9999) + 1))
                .build();

        return deviceRepository.save(device);
    }

    public static Device saveDevice(User user) {

        Device device = Device.createDevice()
                .deviceId("deviceId" + ((int) (Math.random() * 9999) + 1))
                .modelName("IPHONE" + ((int) (Math.random() * 99) + 1))
                .deviceOs(OsType.AOS)
                .fcmToken("4712478v" + ((int) (Math.random() * 9999) + 1))
                .build();

        device.changeDeviceName("거실" + ((int) (Math.random() * 9) + 1));
        device.setDeviceOwner(user.getUserSeq());
        device.connectDevice();

        return deviceRepository.save(device);
    }
}
