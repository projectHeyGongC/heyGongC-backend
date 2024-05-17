package com.heygongc.device.domain.repository.device;

import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.domain.entity.QDevice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    private final DeviceJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    public DeviceRepositoryImpl(DeviceJpaRepository jpaRepository, EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Device save(Device device) {
        return jpaRepository.save(device);
    }

    @Override
    public Optional<Device> findByDeviceId(String deviceId) {
        return jpaRepository.findByDeviceId(deviceId);
    }

    @Override
    public Optional<Device> findMyDevice(String deviceId, Long userSeq) {
        QDevice qDevice = QDevice.device;

        Device device = queryFactory.selectFrom(qDevice)
                .where(qDevice.deviceId.eq(deviceId)
                        .and(qDevice.userSeq.eq(userSeq)))
                .fetchOne();

        return Optional.ofNullable(device);
    }

    @Override
    public List<Device> getDevices(List<String> deviceIds, Long userSeq) {
        QDevice qDevice = QDevice.device;

        List<Device> devices = queryFactory.selectFrom(qDevice)
                .where(qDevice.deviceId.in(deviceIds)
                        .and(qDevice.userSeq.eq(userSeq)))
                .fetch();


        return devices;
    }

    @Override
    public List<Device> getDevices(Long userSeq) {
        return jpaRepository.findAllByUserSeq(userSeq);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}