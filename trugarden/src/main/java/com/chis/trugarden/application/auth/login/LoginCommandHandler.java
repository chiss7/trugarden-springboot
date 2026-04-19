package com.chis.trugarden.application.auth.login;

import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import com.chis.trugarden.application.auth.service.AuthCookieService;
import com.chis.trugarden.application.auth.service.RefreshTokenService;
import com.chis.trugarden.shared.result.Result;
import com.chis.trugarden.shared.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCommandHandler {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final AuthCookieService authCookieService;
    private final RefreshTokenService refreshTokenService;

    @CommandHandler
    public Result<Void> handle(LoginCommand command) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.email(),
                        command.password()
                )
        );
        var claims = new HashMap<String, Object>();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        claims.put("fullName", userDetails.getFullName());
        String accessToken = jwtService.generateToken(claims, userDetails);
        RefreshTokenService.RefreshTokenIssueResult refreshTokenIssue =
                refreshTokenService.issueForNewFamily(userDetails.getDomainUser());

        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        authCookieService.setAccessTokenCookie(response, accessToken);
        authCookieService.setRefreshTokenCookie(response, refreshTokenIssue.rawToken());

        log.info("Login successful for email {}", command.email());
        return Result.success(null);
    }
}
