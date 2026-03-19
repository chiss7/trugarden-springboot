package com.chis.trugarden.application.auth.activate;

import com.chis.trugarden.persistence.user.TokenJpaRepository;
import com.chis.trugarden.persistence.user.UserJpaRepository;
import com.chis.trugarden.persistence.user.entities.TokenEntity;
import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.exception.ExpiredTokenException;
import com.chis.trugarden.shared.exception.InvalidTokenException;
import com.chis.trugarden.shared.exception.UserAlreadyEnabledException;
import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivationCommandHandler {
    private final UserJpaRepository userJpaRepository;
    private final TokenJpaRepository tokenJpaRepository;

    @CommandHandler
    public Result<Void> handle(ActivationCommand command) {
        try {
            TokenEntity savedTokenEntity = tokenJpaRepository.findByToken(command.token())
                    .orElseThrow(() -> new InvalidTokenException("Invalid Token. Please provide another token again"));
            UserEntity userEntity = userJpaRepository.findById(savedTokenEntity.getUserEntity().getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if(userEntity.isEnabled()) throw new UserAlreadyEnabledException("User account is already enabled");
            if(LocalDateTime.now().isAfter(savedTokenEntity.getExpiresAt())) {
                //sendValidationEmail(savedTokenEntity.getUserEntity());
                throw new ExpiredTokenException("Activation token has expired. A new token has been sent");
            }
            userEntity.setEnabled(true);
            userJpaRepository.save(userEntity);
            savedTokenEntity.setValidatedAt(LocalDateTime.now());
            tokenJpaRepository.save(savedTokenEntity);
            return Result.success();
        } catch (Exception e) {
            log.error("Error activating account {}", e.getMessage());
            return Result.failure(Error.failure("ACTIVATE_ACCOUNT_ERROR","Ha ocurrido un error durante la activación de cuenta. Vuelve a intentarlo más tarde."));
        }
    }
}
