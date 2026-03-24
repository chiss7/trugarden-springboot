package com.chis.trugarden.infrastructure.security;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthenticationHelper {

    private AuthenticationHelper() {}

    @NonNull
    public static CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails;
        }
        throw new IllegalStateException("Principal is not CustomUserDetails");
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated()
            && auth.getPrincipal() instanceof CustomUserDetails;
    }
}
