package com.chis.trugarden.application.auth.activate;

import com.chis.trugarden.application.auth.abstractions.TokenRepository;
import com.chis.trugarden.application.auth.service.SendValidationTokenService;
import com.chis.trugarden.application.user.abstractions.UserRepository;
import com.chis.trugarden.domain.user.Token;
import com.chis.trugarden.domain.user.TokenErrors;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.domain.user.UserErrors;
import com.chis.trugarden.shared.exception.ExpiredTokenException;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivationCommandHandler {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final SendValidationTokenService sendValidationTokenService;
    private final Clock clock;

    @CommandHandler
    public Result<Void> handle(ActivationCommand command) {
        Optional<Token> tokenOpt = tokenRepository.findByToken(command.token());
        if (tokenOpt.isEmpty()) {
            return Result.failure(TokenErrors.notFound(command.token()));
        }
        Token token = tokenOpt.get();

        Optional<User> userOpt = userRepository.findByEmail(token.getUser().getEmail().value());
        if (userOpt.isEmpty()) {
            return Result.failure(UserErrors.notFound(token.getUser().getEmail().value()));
        }
        User user = userOpt.get();

        if (user.isEnabled()) {
            return Result.failure(UserErrors.alreadyEnabled(user.getEmail().value()));
        }

        if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
            sendValidationTokenService.sendValidationEmail(token.getUser());
            throw new ExpiredTokenException("El token ha expirado. Se ha enviado un nuevo correo de validación.");
        }

        User enabledUser = user.withEnabled(true);
        userRepository.save(enabledUser);
        Token validatedToken = token.withValidatedAt(LocalDateTime.now(clock));
        tokenRepository.save(validatedToken);
        return Result.success();
    }
}
