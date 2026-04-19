package com.chis.trugarden.application.auth.logout;

import com.chis.trugarden.application.auth.abstractions.RefreshTokenRepository;
import com.chis.trugarden.application.auth.service.AuthCookieService;
import com.chis.trugarden.application.auth.service.RefreshTokenService;
import com.chis.trugarden.domain.auth.RefreshToken;
import com.chis.trugarden.shared.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogoutCommandHandler {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final AuthCookieService authCookieService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Clock clock;

    @CommandHandler
    @Transactional
    public Result<Void> handle(LogoutCommand command) {
        String refreshTokenRaw = authCookieService.extractRefreshToken(request);

        if (refreshTokenRaw != null && !refreshTokenRaw.isBlank()) {
            String tokenHash = refreshTokenService.hash(refreshTokenRaw);
            RefreshToken token = refreshTokenRepository.findByTokenHash(tokenHash).orElse(null);

            if (token != null) {
                LocalDateTime now = LocalDateTime.now(clock);
                refreshTokenRepository.revokeFamily(token.getUserId(), token.getFamilyId(), now, now);
            }
        }

        authCookieService.clearAuthCookies(response);
        return Result.success();
    }
}
