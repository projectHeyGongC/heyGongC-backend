package com.heygongc.notification.domain.repository;

import com.heygongc.notification.domain.entity.Notification;
import com.heygongc.notification.domain.entity.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

    private final JPAQueryFactory queryFactory;


    public CustomNotificationRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Notification> findAllByUserSeq(Long userSeq) {
        QNotification qNotification = QNotification.notification;

        return queryFactory.selectFrom(qNotification)
                .where(qNotification.user.seq.eq(userSeq))
                .orderBy(qNotification.created_at.desc())
                .fetch();
    }

    @Override
    public List<Notification> findAllByUserSeqAndCreatedAt(Long userSeq, String requestAt) throws ParseException {
        QNotification qNotification = QNotification.notification;

        Date date = DateUtils.parseDate(requestAt, "yyyy-MM-dd");
        LocalDateTime startOfDay = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<Notification> notifications = queryFactory.selectFrom(qNotification)
                .where(qNotification.user.seq.eq(userSeq)
                        .and(qNotification.created_at.between(startOfDay, endOfDay)))
                .fetch();

        return notifications;
    }
}
