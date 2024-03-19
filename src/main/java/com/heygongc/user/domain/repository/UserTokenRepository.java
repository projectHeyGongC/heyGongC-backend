package com.heygongc.user.domain.repository;

import com.heygongc.user.domain.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long>, CustomUserTokenRepository {
    Optional<UserToken> findByUserSeq(Long seq);

    Optional<UserToken> findByRefreshToken(String refreshToken);
}
