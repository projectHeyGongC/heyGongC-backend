package com.heygongc.device.domain.repository;

import com.heygongc.device.domain.entity.QDevice;
import com.heygongc.device.domain.entity.Device;
import com.heygongc.device.exception.DeviceNotFoundException;
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
                        .and(qDevice.user.eq(user)))
                .fetchOne();

        return Optional.ofNullable(device);
    }
    @Override
    public long deleteCameraDevices(List<String> deviceIds, User user) {
        QDevice qDevice = QDevice.device;

        // delete 클로저를 사용하여 조건에 맞는 엔트리를 삭제
        long deletedCount = queryFactory.delete(qDevice)
                .where(qDevice.deviceId.in(deviceIds)
                        .and(qDevice.user.eq(user)))
                .execute();

        if (deletedCount == 0) {
            throw new DeviceNotFoundException("No devices found for deletion.");
        }


        return deletedCount; // 삭제된 엔트리의 수를 반환
    }


}