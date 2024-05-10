package com.heygongc.user.domain.repository.usertoken;

import com.heygongc.user.domain.entity.UserToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.heygongc.user.domain.entity.QUserToken.userToken;


public class UserTokenRepositoryImpl implements UserTokenRepository {

    private final UserTokenJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    public UserTokenRepositoryImpl(UserTokenJpaRepository jpaRepository, EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public UserToken save(UserToken userToken) {
        return jpaRepository.save(userToken);
    }

    @Override
    public Optional<UserToken> findByUserSeq(Long userSeq) {
        return jpaRepository.findByUserSeq(userSeq);
    }

    @Override
    public Optional<UserToken> findByRefreshToken(String refreshToken) {
        return jpaRepository.findByRefreshToken(refreshToken);
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
