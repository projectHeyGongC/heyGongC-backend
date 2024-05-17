package com.heygongc.user.domain.repository.usertoken;

import com.heygongc.user.domain.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenJpaRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserSeq(Long userSeq);
    Optional<UserToken> findByRefreshToken(String refreshToken);
}
