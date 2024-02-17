package com.heygongc.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long>, CustomUserTokenRepository {
    Optional<UserToken> findByUserSeq(Long seq);
}
