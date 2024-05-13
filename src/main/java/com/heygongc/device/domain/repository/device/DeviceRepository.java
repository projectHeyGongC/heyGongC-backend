package com.heygongc.device.domain.repository.device;

import com.heygongc.device.domain.entity.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    Device save(Device device);
    Optional<Device> findByDeviceId(String deviceId);
    Optional<Device> findMyDevice(String deviceId, Long userSeq);
    List<Device> getDevices(List<String> deviceIds, Long userSeq);
    List<Device> getDevices(Long userSeq);
    long count();
}
