package com.chis.trugarden.domain.auth;

import java.time.LocalDateTime;
import java.util.Objects;

public class RefreshToken {
    private final Long id;
    private final Long userId;
    private final String familyId;
    private final String tokenHash;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private final LocalDateTime revokedAt;
    private final String replacedByTokenHash;

    public RefreshToken(
            Long id,
            Long userId,
            String familyId,
            String tokenHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime revokedAt,
            String replacedByTokenHash
    ) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId, "El userId no puede ser nulo");
        this.familyId = Objects.requireNonNull(familyId, "El familyId no puede ser nulo");
        this.tokenHash = Objects.requireNonNull(tokenHash, "El tokenHash no puede ser nulo");
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creacion no puede ser nula");
        this.expiresAt = Objects.requireNonNull(expiresAt, "La fecha de expiracion no puede ser nula");
        this.revokedAt = revokedAt;
        this.replacedByTokenHash = replacedByTokenHash;
    }

    public static RefreshToken of(
            Long id,
            Long userId,
            String familyId,
            String tokenHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime revokedAt,
            String replacedByTokenHash
    ) {
        return new RefreshToken(id, userId, familyId, tokenHash, createdAt, expiresAt, revokedAt, replacedByTokenHash);
    }

    public static RefreshToken ofNew(
            Long userId,
            String familyId,
            String tokenHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt
    ) {
        return new RefreshToken(null, userId, familyId, tokenHash, createdAt, expiresAt, null, null);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public String getReplacedByTokenHash() {
        return replacedByTokenHash;
    }

    public boolean isActive(LocalDateTime now) {
        return revokedAt == null && expiresAt.isAfter(now);
    }

    public RefreshToken withRotation(LocalDateTime revokedAt, String replacedByTokenHash) {
        return new RefreshToken(
                this.id,
                this.userId,
                this.familyId,
                this.tokenHash,
                this.createdAt,
                this.expiresAt,
                revokedAt,
                replacedByTokenHash
        );
    }
}
