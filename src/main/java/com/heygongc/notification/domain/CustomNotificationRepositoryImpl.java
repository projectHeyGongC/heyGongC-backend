package com.heygongc.notification.domain;

import com.heygongc.notification.application.NotificationTypeEnum;
import com.heygongc.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

    private final JPAQueryFactory queryFactory;

    private final NotificationRepository notificationRepository;

    public CustomNotificationRepositoryImpl(EntityManager entityManager, NotificationRepository notificationRepository) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.notificationRepository = notificationRepository;

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

    @Override
    @Transactional
    public long deleteOldNotifications(LocalDateTime dateTime, Long userSeq) {
        QNotification qNotification = QNotification.notification;
        long count = queryFactory.delete(qNotification)
                .where(qNotification.created_at.before(dateTime)
                        .and(qNotification.user.seq.eq(userSeq)))
                .execute();
        return count;
    }

    @Override
    @Transactional
    public long deleteExcessNotifications(Long userSeq, NotificationTypeEnum type, int threshold) {
        QNotification qNotification = QNotification.notification;

        // Step 1: Identify the IDs of the top 100 notifications to retain
        List<Long> idsToRetain = queryFactory.select(qNotification.eventSeq)
                .from(qNotification)
                .where(qNotification.user.seq.eq(userSeq)
                        .and(qNotification.typeEnum.eq(type)))
                .orderBy(qNotification.created_at.desc())
                .limit(threshold)
                .fetch();

        // Step 2: Delete notifications for the user and type that are not in the set of IDs to retain
        long deletedCount = queryFactory.delete(qNotification)
                .where(qNotification.user.seq.eq(userSeq)
                        .and(qNotification.typeEnum.eq(type))
                        .and(qNotification.eventSeq.notIn(idsToRetain)))
                .execute();

        return deletedCount;
    }

    @Override
    @Transactional
    public void cleanUpNotifications(Long userSeq) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        for (NotificationTypeEnum type : NotificationTypeEnum.values()) {
            QNotification qNotification = QNotification.notification;

            // Step 1: Check if notifications count exceeds 100 for the type
            long notificationsCount = queryFactory
                    .selectFrom(qNotification)
                    .where(qNotification.user.seq.eq(userSeq)
                            .and(qNotification.typeEnum.eq(type)))
                    .fetchCount();

            // Step 2: Check if there are notifications older than 30 days
            long oldNotificationsCount = queryFactory
                    .selectFrom(qNotification)
                    .where(qNotification.user.seq.eq(userSeq)
                            .and(qNotification.typeEnum.eq(type))
                            .and(qNotification.created_at.before(thirtyDaysAgo)))
                    .fetchCount();

            // If conditions are met, proceed with deletion
            if (notificationsCount > 100 || oldNotificationsCount > 0) {
                // Delete notifications older than 30 days for the specific user
                notificationRepository.deleteOldNotifications(thirtyDaysAgo, userSeq);

                // If more than 100 notifications remain, delete the excess
                if (notificationsCount > 100) {
                    notificationRepository.deleteExcessNotifications(userSeq, type, 100);
                }
            }
        }
    }
}
