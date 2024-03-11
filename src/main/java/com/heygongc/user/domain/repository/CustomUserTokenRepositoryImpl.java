package com.heygongc.user.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import static com.heygongc.user.domain.entity.QUserToken.userToken;


public class CustomUserTokenRepositoryImpl implements CustomUserTokenRepository {

    private final JPAQueryFactory queryFactory;

    public CustomUserTokenRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    @Transactional
    public long deleteToken(Long userSeq) {


        long count = queryFactory.delete(userToken)
                .where(userToken.userSeq.eq(userSeq))
                .execute();

        return count;
    }
}
