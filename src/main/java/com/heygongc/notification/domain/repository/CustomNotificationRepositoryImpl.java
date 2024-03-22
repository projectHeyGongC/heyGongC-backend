package com.heygongc.notification.domain.repository;

import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.entity.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;


public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

    private final JPAQueryFactory queryFactory;


    public CustomNotificationRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Notification> findAllNotificationByUserSeq(Long userSeq) {
        QNotification qNotification = QNotification.notification;

        return queryFactory.selectFrom(qNotification)
                .where(qNotification.user.seq.eq(userSeq))
                .orderBy(qNotification.created_at.desc())
                .fetch();
    }
}
