package com.heygongc.user.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;


public class CustomUserTokenRepositoryImpl implements CustomUserTokenRepository {

    private final JPAQueryFactory queryFactory;

    public CustomUserTokenRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public long deleteToken(Long userSeq) {
        QUserToken qUserToken = QUserToken.userToken;

        long count = queryFactory.delete(qUserToken)
                .where(qUserToken.user.seq.eq(userSeq))
                .execute();

        return count;
    }
}
