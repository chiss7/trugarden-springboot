package com.chis.trugarden.application.auth.service;

import com.chis.trugarden.application.user.abstractions.UserRepository;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.persistence.auth.OAuthCodeJpaRepository;
import com.chis.trugarden.persistence.auth.entities.OAuthCodeEntity;
import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Service for managing temporary OAuth authorization codes.
 * Provides secure code generation and validation for OAuth2 callback flow.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthCodeService {

    private final OAuthCodeJpaRepository oAuthCodeRepository;
    private final UserRepository userRepository;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Value("${trugarden.oauth.code-expiration-minutes:5}")
    private int codeExpirationMinutes;

    /**
     * Generates a temporary authorization code for OAuth2 callback.
     *
     * @param userId the user ID to associate with the code
     * @return the generated authorization code
     */
    @Transactional
    public String generateCode(Long userId) {
        String code = generateSecureCode();
        LocalDateTime now = LocalDateTime.now();

        OAuthCodeEntity codeEntity = OAuthCodeEntity.builder()
                .code(code)
                .userId(userId)
                .createdAt(now)
                .expiresAt(now.plusMinutes(codeExpirationMinutes))
                .used(false)
                .build();

        oAuthCodeRepository.save(codeEntity);

        log.info("Generated OAuth code for userId: {} - Expires at: {}", userId, codeEntity.getExpiresAt());

        return code;
    }

    /**
     * Exchanges a temporary authorization code for the associated user.
     * Marks the code as used and validates expiration.
     *
     * @param code the authorization code
     * @return Result containing the User if successful, or error if code is invalid/expired/used
     */
    @Transactional
    public Result<User> exchangeCode(String code) {
        LocalDateTime now = LocalDateTime.now();

        var codeEntityOpt = oAuthCodeRepository.findByCodeAndUsedFalseAndExpiresAtAfter(code, now);

        if (codeEntityOpt.isEmpty()) {
            log.warn("Invalid or expired OAuth code attempted: {}", code.substring(0, Math.min(10, code.length())));
            return Result.failure(Error.failure("INVALID_CODE", "Invalid or expired authorization code"));
        }

        OAuthCodeEntity codeEntity = codeEntityOpt.get();

        // Mark code as used
        int updated = oAuthCodeRepository.markAsUsed(code, now);
        if (updated == 0) {
            return Result.failure(Error.failure("INVALID_CODE", "Invalid or expired authorization code"));
        }

        // Retrieve user
        var userOpt = userRepository.findById(codeEntity.getUserId());

        if (userOpt.isEmpty()) {
            log.error("User not found for OAuth code - userId: {}", codeEntity.getUserId());
            return Result.failure(Error.notFound("USER_NOT_FOUND", "User not found"));
        }

        log.info("Successfully exchanged OAuth code for userId: {}", codeEntity.getUserId());

        return Result.success(userOpt.get());
    }

    /**
     * Cleans up expired OAuth codes from the database.
     * Should be called periodically (e.g., via scheduled task).
     */
    @Transactional
    public void cleanExpiredCodes() {
        LocalDateTime now = LocalDateTime.now();
        oAuthCodeRepository.deleteByExpiresAtBefore(now);
        log.debug("Cleaned up expired OAuth codes");
    }

    /**
     * Generates a cryptographically secure random code.
     *
     * @return base64-encoded random string (32 characters)
     */
    private String generateSecureCode() {
        byte[] randomBytes = new byte[24]; // 24 bytes = 32 base64 characters
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
