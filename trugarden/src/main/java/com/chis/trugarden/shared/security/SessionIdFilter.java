package com.chis.trugarden.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class SessionIdFilter extends OncePerRequestFilter {

    public static final String SESSION_COOKIE_NAME = "SESSION_ID";
    public static final String SESSION_REQUEST_ATTR = "SESSION_ID_ATTR";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);

        String sessionId = extractSessionId(request);

        // Only generate and set sessionId for unauthenticated users. For authenticated users, we rely on their authentication context.
        if (!isAuthenticated) {

            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString();
                addCookie(response, sessionId);
            }

            request.setAttribute(SESSION_REQUEST_ATTR, sessionId);
        }

        // 🔐 If the user is authenticated, we do NOT set a session ID cookie or request attribute, as we rely on their authentication context for user identification.
        filterChain.doFilter(request, response);
    }

    private String extractSessionId(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void addCookie(HttpServletResponse response, String sessionId) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7); // 7 days
        // cookie.setSecure(true); // REMINDER prod only

        response.addCookie(cookie);
    }
}
