package com.chis.trugarden.application.auth.service;

import com.chis.trugarden.application.auth.abstractions.RefreshTokenRepository;
import com.chis.trugarden.domain.auth.RefreshToken;
import com.chis.trugarden.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenCryptoService refreshTokenCryptoService;
    private final Clock clock;

    @Value("${trugarden.security.refresh.expiration-seconds:2592000}")
    private long refreshTokenExpirationSeconds;

    public RefreshTokenIssueResult issueForNewFamily(User user) {
        String familyId = UUID.randomUUID().toString();
        return issueForFamily(user.getId(), familyId);
    }

    public RefreshTokenIssueResult issueForFamily(Long userId, String familyId) {
        String rawToken = refreshTokenCryptoService.generateTokenValue();
        String tokenHash = refreshTokenCryptoService.hashToken(rawToken);
        LocalDateTime now = LocalDateTime.now(clock);

        RefreshToken toSave = RefreshToken.ofNew(
                userId,
                familyId,
                tokenHash,
                now,
                now.plusSeconds(refreshTokenExpirationSeconds)
        );

        RefreshToken saved = refreshTokenRepository.save(toSave);
        return new RefreshTokenIssueResult(rawToken, saved);
    }

    public String hash(String tokenValue) {
        return refreshTokenCryptoService.hashToken(tokenValue);
    }

    public record RefreshTokenIssueResult(String rawToken, RefreshToken refreshToken) {
    }
}
