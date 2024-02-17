package com.heygongc.notification.domain;

import com.heygongc.notification.application.NotificationTypeEnum;
import com.heygongc.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

    private final JPAQueryFactory queryFactory;


    public CustomNotificationRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Notification> findMyNotification(Long eventSeq, User user) {
        QNotification qNotification = QNotification.notification;

        Notification notification = queryFactory.selectFrom(qNotification)
                .where(qNotification.eventSeq.eq(eventSeq)
                        .and(qNotification.user.eq(user)))
                .fetchOne();

        return Optional.ofNullable(notification);

    }

    @Override
    public List<Notification> findNotificationByType(Long userSeq, NotificationTypeEnum type) {
        QNotification qNotification = QNotification.notification;

        return queryFactory.selectFrom(qNotification)
                .where(qNotification.user.seq.eq(userSeq)
                        .and(qNotification.typeEnum.eq(type)))
                .fetch();
    }

}
