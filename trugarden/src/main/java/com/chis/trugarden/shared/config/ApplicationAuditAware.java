package com.chis.trugarden.shared.config;

import com.chis.trugarden.infrastructure.security.AuthenticationHelper;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        if (!AuthenticationHelper.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.of(AuthenticationHelper.getCurrentUserId());
    }
}
