package com.heygongc.device.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>, CustomDeviceRepository {

    List<Device> findAllByUserSeq(Long userSeq);
    void deleteAllByUserSeq(Long userSeq);
}
