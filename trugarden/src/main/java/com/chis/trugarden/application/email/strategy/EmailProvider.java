package com.chis.trugarden.application.email.strategy;

import com.chis.trugarden.application.email.dtos.EmailMessage;

public interface EmailProvider {
    void send(EmailMessage message);
    String providerName();
}
