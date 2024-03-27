package com.heygongc.video.domain.repository;

import com.heygongc.video.domain.entity.QVideo;
import com.heygongc.video.domain.entity.Video;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class CustomVideoRepositoryImpl implements CustomVideoRepository {

    private final JPAQueryFactory queryFactory;

    public CustomVideoRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Video> findOneByUserSeqAndCreatedAt(Long userSeq, String requestAt) throws ParseException {
        QVideo qVideo = QVideo.video;

        Date date = DateUtils.parseDate(requestAt, "yyyy-MM-dd");
        LocalDateTime startOfDay = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        Video video = queryFactory.selectFrom(qVideo)
                .where(qVideo.user.seq.eq(userSeq)
                        .and(qVideo.created_at.between(startOfDay, endOfDay)))
                .orderBy(qVideo.created_at.desc())
                .fetchOne();

        return Optional.ofNullable(video);
    }
}
