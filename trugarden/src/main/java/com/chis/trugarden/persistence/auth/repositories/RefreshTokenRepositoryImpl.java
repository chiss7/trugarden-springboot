package com.chis.trugarden.persistence.auth.repositories;

import com.chis.trugarden.application.auth.abstractions.RefreshTokenRepository;
import com.chis.trugarden.domain.auth.RefreshToken;
import com.chis.trugarden.persistence.auth.RefreshTokenEntityMapper;
import com.chis.trugarden.persistence.auth.RefreshTokenJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final RefreshTokenEntityMapper refreshTokenEntityMapper;

    public RefreshTokenRepositoryImpl(RefreshTokenJpaRepository refreshTokenJpaRepository, RefreshTokenEntityMapper refreshTokenEntityMapper) {
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
        this.refreshTokenEntityMapper = refreshTokenEntityMapper;
    }

    @Override
    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        var entity = refreshTokenEntityMapper.toEntity(refreshToken);
        return refreshTokenEntityMapper.toDomain(refreshTokenJpaRepository.save(entity));
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHash(tokenHash)
                .map(refreshTokenEntityMapper::toDomain);
    }

    @Override
    public Optional<RefreshToken> findActiveByTokenHash(String tokenHash, LocalDateTime now) {
        return refreshTokenJpaRepository.findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(tokenHash, now)
                .map(refreshTokenEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public int rotateToken(String currentTokenHash, String newTokenHash, LocalDateTime revokedAt, LocalDateTime now) {
        return refreshTokenJpaRepository.rotateToken(currentTokenHash, newTokenHash, revokedAt, now);
    }

    @Override
    @Transactional
    public int revokeFamily(Long userId, String familyId, LocalDateTime revokedAt, LocalDateTime now) {
        return refreshTokenJpaRepository.revokeFamily(userId, familyId, revokedAt, now);
    }
}
