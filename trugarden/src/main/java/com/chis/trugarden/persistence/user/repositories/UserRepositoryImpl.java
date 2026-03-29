package com.chis.trugarden.persistence.user.repositories;

import com.chis.trugarden.application.user.abstractions.UserRepository;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.persistence.user.UserEntityMapper;
import com.chis.trugarden.persistence.user.UserJpaRepository;
import com.chis.trugarden.persistence.user.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userEntityMapper::toDomain)
                .or(() -> {
                    log.warn("User with email {} not found", email);
                    return Optional.empty();
                });
    }

    @Override
    public Optional<User> findByGoogleId(String googleId) {
        return userJpaRepository.findByGoogleId(googleId)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = userEntityMapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return userEntityMapper.toDomain(savedEntity);
    }
}
