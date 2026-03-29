package com.chis.trugarden.infrastructure.security;

import com.chis.trugarden.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

public class CustomUserDetails implements UserDetails, Principal, OidcUser {

    private final User user;
    private final OidcUser oidcUser;

    public CustomUserDetails(User user) {
        this.user = user;
        this.oidcUser = null;
    }

    // for OIDC flow
    public CustomUserDetails(User user, OidcUser oidcUser) {
        this.user = user;
        this.oidcUser = oidcUser;
    }

    public static CustomUserDetails from(User user, OidcUser oidcUser) {
        return new CustomUserDetails(user, oidcUser);
    }

    public static CustomUserDetails from(User user) {
        return new CustomUserDetails(user);
    }

    // ── Principal ─────────────────────────────────────────────────────────────

    @Override
    public String getName() {
        return user.getEmail().value();
    }

    // ── UserDetails ───────────────────────────────────────────────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword().hashedValue();
    }

    @Override
    public String getUsername() {
        return user.getEmail().value();
    }

    // Returns true if the user's account is valid (ie non-expired), false if no longer valid (ie expired)
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired(); //true
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isAccountLocked();
    }

    // Indicates whether the user's credentials (password) has expired. Expired credentials prevent authentication.
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired(); //true
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public Long getId() {
        return user.getId();
    }

    public User getDomainUser() {
        return user;
    }

    public String getFullName() {
        return user.getFullName();
    }

    @Override
    public String toString() {
        return "UserPrincipal{username=" + getUsername() + ", authorities=" + getAuthorities() + "}";
    }

    // ── OidcUser ──────────────────────────────────────────────────────────────

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser != null ? oidcUser.getClaims() : Map.of();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser != null ? oidcUser.getUserInfo() : null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser != null ? oidcUser.getIdToken() : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser != null ? oidcUser.getAttributes() : Map.of();
    }
}
