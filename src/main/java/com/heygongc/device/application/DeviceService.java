package com.heygongc.device.application;

import com.heygongc.device.domain.Device;
import com.heygongc.device.presentation.request.DeviceNameRequest;
import com.heygongc.device.presentation.response.DeviceResponse;

import java.util.List;

public interface DeviceService {
    DeviceResponse addDevice(DeviceNameRequest deviceNameRequest);
    Device getDevice(Long device_seq);

    List<Device> getAllDevices();

    Device updateDevice(DeviceNameRequest deviceNameRequest);

    Boolean deleteDevice(Long device_seq);

    Boolean deleteAllDevices();
}
