package com.heygongc.notification.domain;

import com.heygongc.notification.application.NotificationTypeEnum;
import com.heygongc.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.aspectj.weaver.ast.Not;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

    private final JPAQueryFactory queryFactory;

    public CustomNotificationRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Notification findNotificationBySeqAndUser(Long eventSeq, User user) {
        QNotification qNotification = QNotification.notification;

        Notification notification = queryFactory.selectFrom(qNotification)
                .where(qNotification.eventSeq.eq(eventSeq)
                        .and(qNotification.user.eq(user)))
                .fetchOne();

        return notification;

    }

    @Override
    public List<Notification> findByUserSeqAndType(Long userSeq, NotificationTypeEnum type) {
        QNotification qNotification = QNotification.notification;

        return queryFactory.selectFrom(qNotification)
                .where(qNotification.user.seq.eq(userSeq)
                        .and(qNotification.typeEnum.eq(type)))
                .fetch();
    }
    @Override
    @Transactional
    public long deleteOldNotifications(LocalDateTime dateTime) {
        QNotification qNotification = QNotification.notification;
        long count = queryFactory.delete(qNotification)
                .where(qNotification.created_at.before(dateTime))
                .execute();
        return count;
    }
}
