package com.heygongc.user.domain.repository.user;

import com.heygongc.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserSeq(Long userSeq);
    Optional<User> findBySnsId(String snsId);
    boolean existsBySnsId(String snsId);
}
