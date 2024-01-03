package com.heygongc.device.application;

import com.heygongc.device.domain.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceService {

    Optional<Device> getDevice(Long deviceSeq);

    List<Device> getAllDevices(Long userSeq);

    Device addDevice(Long userSeq, String qrCode, String deviceName);

    Device updateDevice(Long deviceSeq, Long userSeq, String deviceName);

    Boolean deleteDevice(Long deviceSeq, Long userSeq);

    Boolean deleteAllDevices(Long userSeq);


}
