package com.heygongc.video.domain.repository.video;

import com.heygongc.video.domain.entity.QVideo;
import com.heygongc.video.domain.entity.Video;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Repository
public class VideoRepositoryImpl implements VideoRepository {

    private final VideoJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    public VideoRepositoryImpl(VideoJpaRepository jpaRepository, EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Video save(Video video) {
        return jpaRepository.save(video);
    }

    @Override
    public Optional<Video> getVideo(Long userSeq, String requestAt) throws ParseException {
        QVideo qVideo = QVideo.video;

        String r = StringUtils.getDigits(requestAt);
        Date date = DateUtils.parseDate(r, "yyyyMMdd");
        LocalDateTime startOfDay = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        Video video = queryFactory.selectFrom(qVideo)
                .where(qVideo.user.userSeq.eq(userSeq)
                        .and(qVideo.createdAt.between(startOfDay, endOfDay)))
                .orderBy(qVideo.createdAt.desc())
                .fetchOne();

        return Optional.ofNullable(video);
    }
}
