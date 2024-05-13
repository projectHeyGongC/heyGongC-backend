package com.heygongc.notification.domain.repository.notification;

import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.entity.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    public NotificationRepositoryImpl(NotificationJpaRepository jpaRepository, EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Notification save(Notification notification) {
        return jpaRepository.save(notification);
    }

    @Override
    public List<Notification> getNotifications(Long userSeq) {
        QNotification qNotification = QNotification.notification;

        return queryFactory.selectFrom(qNotification)
                .where(qNotification.user.userSeq.eq(userSeq))
                .orderBy(qNotification.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Notification> getNotifications(Long userSeq, String requestAt) throws ParseException {
        QNotification qNotification = QNotification.notification;

        String r = StringUtils.getDigits(requestAt);
        Date date = DateUtils.parseDate(r, "yyyyMMdd");
        LocalDateTime startOfDay = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<Notification> notifications = queryFactory.selectFrom(qNotification)
                .where(qNotification.user.userSeq.eq(userSeq)
                        .and(qNotification.createdAt.between(startOfDay, endOfDay)))
                .orderBy(qNotification.createdAt.asc())
                .fetch();

        return notifications;
    }

    @Override
    public List<Notification> getNotifications(Long userSeq, String deviceId, String requestAt) throws ParseException {
        QNotification qNotification = QNotification.notification;

        String r = StringUtils.getDigits(requestAt);
        Date date = DateUtils.parseDate(r, "yyyyMMdd");
        LocalDateTime startOfDay = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<Notification> notifications = queryFactory.selectFrom(qNotification)
                .where(qNotification.user.userSeq.eq(userSeq)
                        .and(qNotification.device.deviceId.eq(deviceId))
                        .and(qNotification.createdAt.between(startOfDay, endOfDay)))
                .orderBy(qNotification.createdAt.asc())
                .fetch();

        return notifications;
    }
}
