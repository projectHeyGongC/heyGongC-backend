package com.heygongc.device.domain.repository;

import com.heygongc.device.domain.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long>, CustomDeviceRepository {

    List<Device> findAllByUserSeq(Long userSeq);
    void deleteAllByUserSeq(Long userSeq);

    Optional<Device> findByDeviceId(String deviceId);
}
