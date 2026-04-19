package com.chis.trugarden.application.auth.oauth;

import com.chis.trugarden.application.auth.service.AuthCookieService;
import com.chis.trugarden.application.auth.service.OAuthCodeService;
import com.chis.trugarden.application.auth.service.RefreshTokenService;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import com.chis.trugarden.shared.result.Result;
import com.chis.trugarden.shared.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeCodeCommandHandler {
    private final OAuthCodeService oAuthCodeService;
    private final JwtService jwtService;
    private final HttpServletResponse response;
    private final AuthCookieService authCookieService;
    private final RefreshTokenService refreshTokenService;

    @CommandHandler
    public Result<Void> handle(ExchangeCodeCommand command){
        Result<User> userResult = oAuthCodeService.exchangeCode(command.code());

        if (userResult.isFailure()) {
            return Result.failure(userResult.getError());
        }

        User user = userResult.getValue();
        CustomUserDetails userDetails = new CustomUserDetails(user, null);
        String accessToken = jwtService.generateToken(userDetails);
        RefreshTokenService.RefreshTokenIssueResult refreshTokenIssue = refreshTokenService.issueForNewFamily(user);

        authCookieService.setAccessTokenCookie(response, accessToken);
        authCookieService.setRefreshTokenCookie(response, refreshTokenIssue.rawToken());

        return Result.success(null);
    }
}
