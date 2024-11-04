package com.chis.trugarden.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * This method intercepts each HTTP request and applies JWT validation.
     * It checks the Authorization header, extracts the JWT, and validates it.
     * If the JWT is valid, the user's authentication is set in the SecurityContext.
     * Every time we have a request this method will be executed.
     * @param request The HTTP request to filter.
     * @param response The HTTP response.
     * @param filterChain The filter chain which allows the request to proceed.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException If an input/output error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Skip the filter for authentication-related paths (e.g., login and registration)
        if(request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response); // Continue to the next filter
            return; // Skip further JWT processing
        }

        // Get the Authorization header from the request (contains the JWT)
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String userEmail;

        // If the Authorization header is missing or doesn't start with "Bearer", skip filtering
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Continue to the next filter
            return;
        }

        // Extract the JWT from the Authorization header by removing the "Bearer " prefix
        jwt = authHeader.substring(7);

        // Extract the user email (subject) from the JWT
        userEmail = jwtService.extractUsername(jwt);

        // If the JWT contains a valid user email and no authentication is present in the SecurityContext
        // this means the user is not authenticated
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details using the extracted email (subject)
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // Validate the JWT against the user details
            if(jwtService.isTokenValid(jwt, userDetails)) {
                // Create an authentication token for the user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // Principal (user details)
                        null, // Credentials are not required for token-based authentication
                        userDetails.getAuthorities() // Authorities (roles/permissions) assigned to the user
                );
                // Attach additional details (such as remote IP and session ID) to the authentication token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Set the authenticated user in the SecurityContext to indicate that the user is authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continue with the filter chain, allowing the request to proceed further
        filterChain.doFilter(request, response);
    }
}
