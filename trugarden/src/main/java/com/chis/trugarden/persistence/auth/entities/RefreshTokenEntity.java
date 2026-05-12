package com.chis.trugarden.persistence.auth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "refresh_token", indexes = {
        @Index(name = "idx_refresh_token_hash", columnList = "tokenHash", unique = true),
        @Index(name = "idx_refresh_token_family", columnList = "userId,familyId")
})
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 64)
    private String familyId;

    @Column(nullable = false, unique = true, length = 128)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime revokedAt;

    @Column(length = 128)
    private String replacedByTokenHash;

    public RefreshTokenEntity() {
    }

    public RefreshTokenEntity(Long id, Long userId, String familyId, String tokenHash, LocalDateTime createdAt, LocalDateTime expiresAt, LocalDateTime revokedAt, String replacedByTokenHash) {
        this.id = id;
        this.userId = userId;
        this.familyId = familyId;
        this.tokenHash = tokenHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.replacedByTokenHash = replacedByTokenHash;
    }

}
