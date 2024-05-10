package com.heygongc.user.domain.repository.user;

import com.heygongc.user.domain.entity.User;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }

    @Override
    public Optional<User> findByUserSeq(Long userSeq) {
        return jpaRepository.findByUserSeq(userSeq);
    }

    @Override
    public Optional<User> findBySnsId(String snsId) {
        return jpaRepository.findBySnsId(snsId);
    }

    @Override
    public boolean existsBySnsId(String snsId) {
        return jpaRepository.existsBySnsId(snsId);
    }
}
