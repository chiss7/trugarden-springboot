package com.chis.trugarden.application.auth.service;

import com.chis.trugarden.application.auth.abstractions.TokenRepository;
import com.chis.trugarden.application.email.EmailService;
import com.chis.trugarden.domain.user.Token;
import com.chis.trugarden.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SendValidationTokenService {
    private final EmailService emailService;
    private final Clock clock;
    private final TokenRepository tokenRepository;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void sendValidationEmail(User user) {
        String newToken = generateAndSaveActivationToken(user);
        emailService.sendActivateAccountEmail(
                user.getEmail().value(),
                user.getFullName(),
                activationUrl,
                newToken
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationToken(6);
        Token token = Token.ofNew(
                generatedToken,
                LocalDateTime.now(clock),
                LocalDateTime.now(clock).plusMinutes(15),
                user
        );
        return tokenRepository.save(token).getToken();
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
