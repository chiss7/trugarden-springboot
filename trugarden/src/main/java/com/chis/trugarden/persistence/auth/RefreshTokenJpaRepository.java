package com.chis.trugarden.persistence.auth;

import com.chis.trugarden.persistence.auth.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    Optional<RefreshTokenEntity> findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(String tokenHash, LocalDateTime now);

    @Modifying
    @Query("""
        update RefreshTokenEntity t
           set t.revokedAt = :revokedAt,
               t.replacedByTokenHash = :newTokenHash
         where t.tokenHash = :currentTokenHash
           and t.revokedAt is null
           and t.expiresAt > :now
    """)
    int rotateToken(
            @Param("currentTokenHash") String currentTokenHash,
            @Param("newTokenHash") String newTokenHash,
            @Param("revokedAt") LocalDateTime revokedAt,
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Query("""
        update RefreshTokenEntity t
           set t.revokedAt = :revokedAt
         where t.userId = :userId
           and t.familyId = :familyId
           and t.revokedAt is null
           and t.expiresAt > :now
    """)
    int revokeFamily(
            @Param("userId") Long userId,
            @Param("familyId") String familyId,
            @Param("revokedAt") LocalDateTime revokedAt,
            @Param("now") LocalDateTime now
    );
}
