package com.chis.trugarden.application.email.service;

import com.chis.trugarden.application.email.dtos.EmailMessage;
import com.chis.trugarden.application.email.factory.EmailProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailProviderFactory providerFactory;

    @Async
    public void send(EmailMessage message) {
        log.info("Enviando email a {} con asunto: {}", message.to(), message.subject());
        providerFactory.getProvider().send(message);
    }
}
