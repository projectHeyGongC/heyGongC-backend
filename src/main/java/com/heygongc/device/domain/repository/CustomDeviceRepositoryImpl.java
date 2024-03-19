package com.heygongc.device.domain.repository;

import com.heygongc.device.domain.entity.QDevice;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.user.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class CustomDeviceRepositoryImpl implements CustomDeviceRepository {

    private final JPAQueryFactory queryFactory;

    public CustomDeviceRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Device> findMyDevice(Long deviceSeq, User user) {
        QDevice qDevice = QDevice.device;

        Device device = queryFactory.selectFrom(qDevice)
                .where(qDevice.deviceSeq.eq(deviceSeq)
                        .and(qDevice.user.eq(user)))
                .fetchOne();

        return Optional.ofNullable(device);
    }
}