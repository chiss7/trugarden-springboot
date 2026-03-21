package com.chis.trugarden.domain.user;

import java.time.LocalDateTime;
import java.util.Objects;

public class Token {
    private final Long id;
    private final String token;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private final LocalDateTime validatedAt;
    private final User user;

    public Token(
            Long id,
            String token,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime validatedAt,
            User user
    ) {
        this.id = id;
        this.token = Objects.requireNonNull(token, "El token no puede ser nulo");
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creación no puede ser nula");
        this.expiresAt = Objects.requireNonNull(expiresAt, "La fecha de expiración no puede ser nula");
        this.validatedAt = validatedAt;
        this.user = user;
    }

    public static Token of(
            Long id,
            String token,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            LocalDateTime validatedAt,
            User user
    ) {
        return new Token(id, token, createdAt, expiresAt, validatedAt, user);
    }

    public static Token ofNew(
            String token,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            User user
    ) {
        return new Token(null, token, createdAt, expiresAt, null, user);
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getValidatedAt() {
        return validatedAt;
    }

    public User getUser() {
        return user;
    }

    public Token withValidatedAt(LocalDateTime validatedAt) {
        return new Token(this.id, this.token, this.createdAt, this.expiresAt, validatedAt, this.user);
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", validatedAt=" + validatedAt +
                ", user=" + user +
                '}';
    }
}
