package com.chis.trugarden.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /**
     * Extracts the username (subject) from the JWT token.
     * @param jwt The JWT token from which to extract the username.
     * @return The username (subject) embedded in the JWT.
     */
    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token.
     * @param <T> The type of claim to be returned.
     * @param jwt The JWT token.
     * @param claimResolver A function to specify which claim to extract.
     * @return The desired claim extracted from the JWT.
     */
    private <T> T extractClaim(String jwt, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(jwt); // Extract all claims from the token
        return claimResolver.apply(claims); // Use the function to retrieve the specific claim
    }

    /**
     * Extracts all claims from the JWT token.
     * @param jwt The JWT token from which to extract claims.
     * @return A Claims object containing all claims in the token.
     */
    private Claims extractAllClaims(String jwt) {
        // Parse the JWT token using the signing key and return the claims (the body of the token)
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt) // Parse the token and extract the claims
                .getBody();
    }

    /**
     * Generates a JWT token for a given user without extra claims.
     * @param userDetails The user details used to generate the token.
     * @return A signed JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with custom claims.
     * @param claims Additional claims to include in the token.
     * @param userDetails The user details used to generate the token.
     * @return A signed JWT token.
     */
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpiration);
    }

    /**
     * Builds and signs the JWT token with claims, user details, and expiration time.
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The user details used to generate the token.
     * @param jwtExpiration Expiration time for the token.
     * @return A signed JWT token.
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long jwtExpiration) {
        // Extract user roles/authorities
        var authorities = userDetails.getAuthorities()
                .stream()
                // Map authorities to strings
                .map(GrantedAuthority::getAuthority)
                .toList();

        // Build the JWT token by setting claims, subject, expiration, etc.
        return Jwts
                .builder()
                .setClaims(extraClaims) // Set additional claims
                .setSubject(userDetails.getUsername()) // Set the subject (user email)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issue time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Set the expiration time
                .claim("authorities", authorities) // Include user authorities in the claims
                .signWith(getSignInKey()) // Sign the token with the secret key
                .compact(); // Return the compact (string) version of the token
    }

    /**
     * Retrieves the signing key used to sign and verify JWT tokens.
     * @return The signing key generated from the secret key.
     */
    private Key getSignInKey() {
        // Decode the Base64-encoded secret key and generate the HMAC-SHA key
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes); // Generate the HMAC key using the decoded secret
    }

    /**
     * Validates the token by checking its subject and expiration status.
     * @param jwt The JWT token to validate.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String userEmail = extractUsername(jwt);
        // Check if the token's username matches the user's and if it's not expired
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(jwt);
    }

    /**
     * Checks if the token is expired.
     * @param jwt The JWT token to check.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String jwt) {
        // Extract the expiration date and check if it has passed
        return extractExpiration(jwt).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     * @param jwt The JWT token from which to extract the expiration date.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String jwt) {
        // Use the Claims object to extract the expiration claim
        return extractClaim(jwt, Claims::getExpiration);
    }
}
