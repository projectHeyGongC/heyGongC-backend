package com.heygongc.device.domain.repository;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.user.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface CustomDeviceRepository {
    Optional<Device> findMyDevice(String deviceId, User user);

    List<Device> findAllDevices(List<String> deviceIds, User user);

}