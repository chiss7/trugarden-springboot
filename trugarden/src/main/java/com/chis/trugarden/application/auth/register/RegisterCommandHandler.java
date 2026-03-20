package com.chis.trugarden.application.auth.register;

import com.chis.trugarden.application.email.EmailService;
import com.chis.trugarden.persistence.role.RoleJpaRepository;
import com.chis.trugarden.persistence.role.entities.RoleEntity;
import com.chis.trugarden.persistence.user.TokenJpaRepository;
import com.chis.trugarden.persistence.user.UserJpaRepository;
import com.chis.trugarden.persistence.user.UserMapper;
import com.chis.trugarden.persistence.user.entities.TokenEntity;
import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.enums.Roles;
import com.chis.trugarden.shared.exception.PasswordMismatchException;
import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterCommandHandler {
    private final RoleJpaRepository roleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final TokenJpaRepository tokenJpaRepository;
    private final EmailService emailService;
    private final UserMapper mapper;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @CommandHandler
    public Result<Long> handle(RegisterCommand command) {
        try {
            RoleEntity userRoleEntity = roleJpaRepository.findByName(Roles.ROLE_CUSTOMER)
                    .orElseThrow(() -> new IllegalStateException("Role Customer was not initialized"));
            if(!command.password().equals(command.confirmPassword())) {
                throw new PasswordMismatchException("Passwords do not match");
            }
            UserEntity userEntity = mapper.toUser(command, userRoleEntity);
            UserEntity savedUser = userJpaRepository.save(userEntity);
            sendValidationEmail(userEntity);
            return Result.success(savedUser.getId());
        } catch (Exception e) {
            log.error("Error during register user {}: {}", command.email(), e.getMessage());
            return Result.failure(Error.failure("REGISTER_ERROR","Ha ocurrido un error durante el registro. Vuelve a intentarlo más tarde."));
        }
    }

    private void sendValidationEmail(UserEntity userEntity) {
        String newToken = generateAndSaveActivationToken(userEntity);
        emailService.sendActivateAccountEmail(
                userEntity.getEmail(),
                userEntity.getFullName(),
                activationUrl,
                newToken
        );
    }

    private String generateAndSaveActivationToken(UserEntity userEntity) {
        String generatedToken = generateActivationToken(6);
        TokenEntity tokenEntity = TokenEntity.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .userEntity(userEntity)
                .build();
        tokenJpaRepository.save(tokenEntity);
        return generatedToken;
    }

    private String generateActivationToken(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        // SecureRandom will make sure that the generated random value is cryptographically secure
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length()); // 0..9
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
