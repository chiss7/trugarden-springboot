package com.chis.trugarden.shared.security;

import com.chis.trugarden.shared.util.RequestUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

/**
 * Rate limiting filter using token bucket algorithm (Bucket4j + Caffeine).
 *
 * Two tiers:
 *  - SENSITIVE endpoints (auth/activate, login, etc.): strict limit, intervally refill.
 *  - General endpoints: relaxed limit, greedy refill.
 *
 * Caffeine cache with TTL prevents unbounded memory growth in production.
 */
@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final List<String> SENSITIVE_PATHS = List.of(
            "/api/v1/auth/activate",
            "/api/v1/auth/authenticate",
            "/api/v1/auth/oauth/exchange"
    );

    @Value("${trugarden.rate-limit.requests-per-minute:60}")
    private int requestsPerMinute;

    @Value("${trugarden.rate-limit.sensitive-requests-per-minute:5}")
    private int sensitiveRequestsPerMinute;

    // Expires 10 min after last access; capped at 100k entries (~a few MB)
    private final Cache<String, Bucket> generalBuckets = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .maximumSize(100_000)
            .build();

    private final Cache<String, Bucket> sensitiveBuckets = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .maximumSize(100_000)
            .build();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String clientIp = RequestUtils.getClientIp(request);
        String path = request.getRequestURI();
        boolean isSensitive = SENSITIVE_PATHS.stream().anyMatch(path::startsWith);

        Bucket bucket = isSensitive
                ? sensitiveBuckets.get(clientIp, k -> createSensitiveBucket())
                : generalBuckets.get(clientIp, k -> createGeneralBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit exceeded — IP: {} | Path: {} | Sensitive: {} | User-Agent: {}",
                    clientIp, path, isSensitive, request.getHeader("User-Agent"));

            response.setStatus(429);
            response.setHeader("Retry-After", "60");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"error\": \"Demasiadas solicitudes. Por favor espera un momento.\", \"retryAfter\": 60}"
            );
        }
    }

    /**
     * General bucket: 60 req/min with greedy refill (1 token/sec).
     * Allows moderate bursting — good for normal browsing.
     */
    private Bucket createGeneralBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(requestsPerMinute)
                        .refillGreedy(requestsPerMinute, Duration.ofMinutes(1))
                )
                .build();
    }

    /**
     * Sensitive bucket: 5 req/min with intervally refill.
     * All tokens reload at once after the full minute — no burst allowed.
     * At 5 req/min over 15 min (OTP lifetime) = 75 attempts max per IP.
     */
    private Bucket createSensitiveBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(sensitiveRequestsPerMinute)
                        .refillIntervally(sensitiveRequestsPerMinute, Duration.ofMinutes(1))
                )
                .build();
    }
}