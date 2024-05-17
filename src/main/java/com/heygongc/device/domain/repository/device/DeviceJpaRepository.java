package com.heygongc.device.domain.repository.device;

import com.heygongc.device.domain.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceJpaRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByDeviceId(String deviceId);
    List<Device> findAllByUserSeq(Long userSeq);
}