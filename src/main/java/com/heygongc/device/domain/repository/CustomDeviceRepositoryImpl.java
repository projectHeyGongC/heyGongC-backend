package com.heygongc.device.domain.repository;

import com.heygongc.device.domain.entity.QDevice;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.user.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CustomDeviceRepositoryImpl implements CustomDeviceRepository {

    private final JPAQueryFactory queryFactory;

    public CustomDeviceRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Device> findMyDevice(String deviceId, User user) {
        QDevice qDevice = QDevice.device;

        Device device = queryFactory.selectFrom(qDevice)
                .where(qDevice.deviceId.eq(deviceId)
                        .and(qDevice.userSeq.eq(user.getUserSeq())))
                .fetchOne();

        return Optional.ofNullable(device);
    }

    @Override
    public List<Device> findAllDevices(List<String> deviceIds, User user) {
        QDevice qDevice = QDevice.device;

        // 조건에 맞는 Device 엔티티 리스트 조회
        List<Device> devices = queryFactory.selectFrom(qDevice)
                .where(qDevice.deviceId.in(deviceIds)
                        .and(qDevice.userSeq.eq(user.getUserSeq())))
                .fetch();


        return devices; // 조건에 해당하는 디바이스 리스트 반환
    }


}