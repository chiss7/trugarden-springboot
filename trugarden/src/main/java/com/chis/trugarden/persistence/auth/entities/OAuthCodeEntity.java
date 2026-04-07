package com.chis.trugarden.persistence.auth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Temporary OAuth authorization code entity.
 * Used to securely exchange OAuth2 callback for JWT token without exposing JWT in URL.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "oauth_code")
public class OAuthCodeEntity {

    @Id
    private String code;

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean used;
}
