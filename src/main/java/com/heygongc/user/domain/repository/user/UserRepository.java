package com.heygongc.user.domain.repository.user;

import com.heygongc.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUserSeq(Long userSeq);
    Optional<User> findBySnsId(String snsId);
    boolean existsBySnsId(String snsId);
}
