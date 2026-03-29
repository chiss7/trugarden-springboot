package com.chis.trugarden.application.email.strategy;

import com.chis.trugarden.application.email.dtos.EmailMessage;
import com.chis.trugarden.shared.exception.EmailSendException;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResendEmailProvider implements EmailProvider {

    private final Resend resend;
    private final String fromAddress;

    public ResendEmailProvider(
            @Value("${trugarden.email.api-key}") String apiKey,
            @Value("${trugarden.email.from}") String fromAddress
    ) {
        this.resend = new Resend(apiKey);
        this.fromAddress = fromAddress;
    }

    @Override
    public void send(EmailMessage message) {
        try {
            CreateEmailOptions options = CreateEmailOptions.builder()
                    .from(fromAddress)
                    .to(message.to())
                    .subject(message.subject())
                    .html(message.htmlBody())
                    .build();

            var response = resend.emails().send(options);
            log.info("Email enviado via Resend. ID: {}", response.getId());

        } catch (ResendException e) {
            log.error("Error al enviar email via Resend: {}", e.getMessage());
            throw new EmailSendException("Fallo al enviar email con Resend");
        }
    }

    @Override
    public String providerName() {
        return "resend";
    }
}
