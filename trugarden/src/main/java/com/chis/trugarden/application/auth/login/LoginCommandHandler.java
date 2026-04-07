package com.chis.trugarden.application.auth.login;

import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import com.chis.trugarden.shared.result.Result;
import com.chis.trugarden.shared.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
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

    @CommandHandler
    public Result<LoginResult> handle(LoginCommand command) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.email(),
                        command.password()
                )
        );
        var claims = new HashMap<String, Object>();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        claims.put("fullName", userDetails.getFullName());
        var jwtToken = jwtService.generateToken(claims, userDetails);

        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        log.info("Login successful for email {}", command.email());
        return Result.success(new LoginResult(jwtToken));
    }
}
