package com.heygongc.device.domain;

import com.heygongc.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class CustomDeviceRepositoryImpl implements CustomDeviceRepository {

    private final JPAQueryFactory queryFactory;

    public CustomDeviceRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Device findDeviceBySeqAndUser(Long deviceSeq, User user) {
        QDevice qDevice = QDevice.device;

        Device device = queryFactory.selectFrom(qDevice)
                .where(qDevice.deviceSeq.eq(deviceSeq)
                        .and(qDevice.user.eq(user)))
                .fetchOne();

        return device;
    }
}