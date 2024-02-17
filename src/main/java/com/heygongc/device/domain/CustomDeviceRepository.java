package com.heygongc.device.domain;

import com.heygongc.user.domain.User;

import java.util.Optional;

public interface CustomDeviceRepository {
    Optional<Device> findMyDevice(Long deviceSeq, User user);
}