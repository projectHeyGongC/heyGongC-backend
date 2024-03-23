package com.heygongc.user.domain.repository;

import com.heygongc.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySnsId(String snsId);

    Optional<User> findBySeq(Long seq);


    boolean existsBySnsId(String snsId);
}
