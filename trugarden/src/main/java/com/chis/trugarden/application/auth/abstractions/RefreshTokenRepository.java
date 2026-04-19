package com.chis.trugarden.application.auth.abstractions;

import com.chis.trugarden.domain.auth.RefreshToken;
import org.jmolecules.ddd.annotation.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    Optional<RefreshToken> findActiveByTokenHash(String tokenHash, LocalDateTime now);

    int rotateToken(String currentTokenHash, String newTokenHash, LocalDateTime revokedAt, LocalDateTime now);

    int revokeFamily(Long userId, String familyId, LocalDateTime revokedAt, LocalDateTime now);
}
