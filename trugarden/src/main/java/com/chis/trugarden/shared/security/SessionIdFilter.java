package com.chis.trugarden.shared.security;

import com.chis.trugarden.shared.util.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
public class SessionIdFilter extends OncePerRequestFilter {

    public static final String SESSION_COOKIE_NAME = "SESSION_ID";
    public static final String SESSION_REQUEST_ATTR = "SESSION_ID_ATTR";

    @Value("${trugarden.session.secret-key}")
    private String secretKey;

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
                String signedSessionId = signSessionId(sessionId);
                addCookie(response, signedSessionId);
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
                String sessionId = verifyAndExtractSessionId(cookie.getValue(), request);
                if (sessionId == null) {
                    log.warn("Rejected invalid SESSION_ID cookie from IP: {} - User-Agent: {}",
                            RequestUtils.getClientIp(request),
                            request.getHeader("User-Agent"));
                }
                return sessionId;
            }
        }
        return null;
    }

    /**
     * Signs a session ID with HMAC-SHA256 to prevent tampering.
     * Format: sessionId.signature
     */
    private String signSessionId(String sessionId) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            mac.init(keySpec);
            byte[] signature = mac.doFinal(sessionId.getBytes(StandardCharsets.UTF_8));
            String signatureBase64 = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(signature);
            return sessionId + "." + signatureBase64;
        } catch (Exception e) {
            throw new RuntimeException("Error signing session ID", e);
        }
    }

    /**
     * Verifies the signature and extracts the session ID if valid.
     * Returns null if the signature is invalid or the format is incorrect.
     */
    private String verifyAndExtractSessionId(String signedValue, HttpServletRequest request) {
        if (signedValue == null || !signedValue.contains(".")) {
            log.warn("SESSION_ID cookie missing signature separator - Possible forgery attempt from IP: {} - Cookie value length: {}",
                    RequestUtils.getClientIp(request),
                    signedValue != null ? signedValue.length() : 0);
            return null;
        }

        int lastDot = signedValue.lastIndexOf('.');
        String sessionId = signedValue.substring(0, lastDot);
        String providedSignature = signedValue.substring(lastDot + 1);

        if (!isValidUUID(sessionId)) {
            log.warn("SESSION_ID cookie contains invalid UUID format - Possible forgery attempt from IP: {} - Value: {}",
                    RequestUtils.getClientIp(request),
                    sessionId.length() > 50 ? sessionId.substring(0, 50) + "..." : sessionId);
            return null;
        }

        String expectedSigned = signSessionId(sessionId);
        String expectedSignature = expectedSigned.substring(expectedSigned.lastIndexOf('.') + 1);

        // prevents timing attacks
        if (MessageDigest.isEqual(
                providedSignature.getBytes(StandardCharsets.UTF_8),
                expectedSignature.getBytes(StandardCharsets.UTF_8))) {
            return sessionId;
        }
        
        log.warn("SESSION_ID signature verification failed - Possible forgery/tampering attempt from IP: {} - SessionId: {}",
                RequestUtils.getClientIp(request),
                sessionId);
        return null;
    }

    /**
     * Validates if a string is a valid UUID format.
     */
    private boolean isValidUUID(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void addCookie(HttpServletResponse response, String signedSessionId) {
        String cookieValue = String.format(
                "%s=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Strict",
                SESSION_COOKIE_NAME,
                signedSessionId,
                60 * 60 * 24 * 7  // 7 days
        );
        // TODO: In production, add Secure flag: + "; Secure"
        response.addHeader("Set-Cookie", cookieValue);
    }
}
