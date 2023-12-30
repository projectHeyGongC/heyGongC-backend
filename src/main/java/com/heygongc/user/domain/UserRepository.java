package com.heygongc.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySnsId(String snsId);
    boolean existsBySnsId(String snsId);
}
