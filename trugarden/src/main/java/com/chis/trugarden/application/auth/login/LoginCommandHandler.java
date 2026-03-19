package com.chis.trugarden.application.auth.login;

import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;
import com.chis.trugarden.shared.security.JwtService;
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

    @CommandHandler
    public Result<LoginResult> handle(LoginCommand command) {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            command.email(),
                            command.password()
                    )
            );
            var claims = new HashMap<String, Object>();
            UserEntity userEntity = (UserEntity) auth.getPrincipal();
            claims.put("fullName", userEntity.getFullName());
            var jwtToken = jwtService.generateToken(claims, userEntity);
            log.info("Login successful for email {}", command.email());
            return Result.success(new LoginResult(jwtToken));
        } catch (Exception e) {
            log.error("Error during login for email {}: {}", command.email(), e.getMessage());
            return Result.failure(Error.failure("LOGIN_ERROR","Ha ocurrido un error durante el inicio de sesión. Vuelve a intentarlo más tarde."));
        }
    }
}
