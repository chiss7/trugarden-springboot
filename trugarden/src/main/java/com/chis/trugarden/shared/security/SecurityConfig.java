package com.chis.trugarden.shared.security;

import com.chis.trugarden.application.auth.service.OAuthCodeService;
import com.chis.trugarden.infrastructure.security.CustomOAuth2UserService;
import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtAuthFilter;
    private final SessionIdFilter sessionIdFilter;
    private final RateLimitFilter rateLimitFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuthCodeService oAuthCodeService;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${application.frontend.oauth-callback-url}")
    private String frontendOAuthCallbackUrl;

    // ─── Chain 1: OAuth2 ──────────────────────────────────────────────────────
    @Bean
    @Order(1)
    public SecurityFilterChain oauth2FilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/oauth2/**", "/login/oauth2/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> {
                    headers
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                            .xssProtection(xss -> xss.headerValue(
                                    XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                            .contentSecurityPolicy(csp -> csp
                                    .policyDirectives("default-src 'self'; frame-ancestors 'none'"));
                    if ("prod".equalsIgnoreCase(activeProfile)) {
                        headers.httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                                .requestMatcher(ServletRequest::isSecure)
                        );
                    }
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(req -> req
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOAuth2UserService))
                        .successHandler(oauth2SuccessHandler())
                )
                .build();
    }

    // ─── Chain 2: JWT + SessionId + Rate Limiting ────────────────────────────
    @Bean
    @Order(2)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> {
                    headers
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                            .xssProtection(xss -> xss.headerValue(
                                    XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                            .contentSecurityPolicy(csp -> csp
                                    .policyDirectives("default-src 'self'; frame-ancestors 'none'"));
                    if ("prod".equalsIgnoreCase(activeProfile)) {
                        headers.httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                                .requestMatcher(ServletRequest::isSecure)
                        );
                    }
                })
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((req, res, e) ->
                                writeJsonResponse(res, 401, "No autorizado")
                        )
                        .accessDeniedHandler((req, res, e) ->
                                writeJsonResponse(res, 403, "No tienes los permisos necesarios")
                        )
                )
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/auth/me").authenticated()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/cart/**").permitAll()
                        .requestMatchers("/product/**").permitAll()
                        .requestMatchers("/category/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(sessionIdFilter, JwtFilter.class)
                .build();
    }

    private AuthenticationSuccessHandler oauth2SuccessHandler() {
        return (request, response, authentication) -> {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();

            var session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Generate temporary code instead of JWT
            String code = oAuthCodeService.generateCode(userId);

            // Redirect with code instead of token
            response.sendRedirect(frontendOAuthCallbackUrl + "?code=" + code);
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "SESSION-ID"
        ));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private void writeJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
