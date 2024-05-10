package com.heygongc.user.domain.repository.usertoken;

import com.heygongc.user.domain.entity.UserToken;

import java.util.Optional;

public interface UserTokenRepository {
    UserToken save(UserToken userToken);
    Optional<UserToken> findByUserSeq(Long userSeq);
    Optional<UserToken> findByRefreshToken(String refreshToken);
    long deleteToken(Long userSeq);
}
