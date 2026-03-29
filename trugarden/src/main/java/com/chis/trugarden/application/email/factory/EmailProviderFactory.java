package com.chis.trugarden.application.email.factory;

import com.chis.trugarden.application.email.strategy.EmailProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EmailProviderFactory {

    private final Map<String, EmailProvider> providers;
    private final String activeProvider;

    public EmailProviderFactory(
            List<EmailProvider> providers,
            @Value("${trugarden.email.provider}") String activeProvider) {

        this.providers = providers.stream().collect(Collectors.toMap(EmailProvider::providerName, Function.identity()));
        this.activeProvider = activeProvider;
    }

    public EmailProvider getProvider() {
        EmailProvider provider = providers.get(activeProvider);
        if (provider == null) {
            throw new IllegalStateException(
                    "Email provider no encontrado: " + activeProvider);
        }
        return provider;
    }
}
