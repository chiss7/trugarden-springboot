package com.chis.trugarden.application.auth.refresh;

import com.chis.trugarden.application.auth.abstractions.RefreshTokenRepository;
import com.chis.trugarden.application.auth.service.AuthCookieService;
import com.chis.trugarden.application.auth.service.RefreshTokenService;
import com.chis.trugarden.application.user.abstractions.UserRepository;
import com.chis.trugarden.domain.auth.RefreshToken;
import com.chis.trugarden.domain.auth.RefreshTokenErrors;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.domain.user.UserErrors;
import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import com.chis.trugarden.shared.result.Result;
import com.chis.trugarden.shared.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenCommandHandler {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final AuthCookieService authCookieService;
    private final JwtService jwtService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Clock clock;

    @CommandHandler
    @Transactional
    public Result<Void> handle(RefreshTokenCommand command) {
        String refreshTokenRaw = authCookieService.extractRefreshToken(request);
        if (refreshTokenRaw == null || refreshTokenRaw.isBlank()) {
            authCookieService.clearAuthCookies(response);
            return Result.failure(RefreshTokenErrors.missingCookie());
        }

        String currentTokenHash = refreshTokenService.hash(refreshTokenRaw);
        LocalDateTime now = LocalDateTime.now(clock);

        RefreshToken currentToken = refreshTokenRepository.findByTokenHash(currentTokenHash)
                .orElse(null);

        if (currentToken == null) {
            authCookieService.clearAuthCookies(response);
            return Result.failure(RefreshTokenErrors.invalidToken());
        }

        if (currentToken.getRevokedAt() != null) {
            // Reuse detection
            log.error("Token reuse detected for userId={}, familyId={}", currentToken.getUserId(), currentToken.getFamilyId());
            refreshTokenRepository.revokeFamily(
                    currentToken.getUserId(),
                    currentToken.getFamilyId(),
                    now,
                    now
            );

            authCookieService.clearAuthCookies(response);

            return Result.failure(RefreshTokenErrors.invalidToken());
        }

        if (!currentToken.isActive(now)) {
            authCookieService.clearAuthCookies(response);
            return Result.failure(RefreshTokenErrors.expiredToken());
        }

        User user = userRepository.findById(currentToken.getUserId())
                .orElse(null);

        if (user == null) {
            authCookieService.clearAuthCookies(response);
            return Result.failure(UserErrors.notFound("id=" + currentToken.getUserId()));
        }

        RefreshTokenService.RefreshTokenIssueResult issued =
                refreshTokenService.issueForFamily(user.getId(), currentToken.getFamilyId());

        int rotated = refreshTokenRepository.rotateToken(
                currentTokenHash,
                issued.refreshToken().getTokenHash(),
                now,
                now
        );

        if (rotated == 0) {
            refreshTokenRepository.revokeFamily(
                    user.getId(),
                    currentToken.getFamilyId(),
                    now,
                    now
            );

            authCookieService.clearAuthCookies(response);
            return Result.failure(RefreshTokenErrors.invalidToken());
        }

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("fullName", user.getFullName());
        String accessToken = jwtService.generateToken(claims, new CustomUserDetails(user));

        authCookieService.setAccessTokenCookie(response, accessToken);
        authCookieService.setRefreshTokenCookie(response, issued.rawToken());

        return Result.success();
    }
}
