package com.heygongc.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserSeq(Long seq);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserToken WHERE user.seq = :value")
    void deleteByUserSeq(@Param("value") Long seq);
}
