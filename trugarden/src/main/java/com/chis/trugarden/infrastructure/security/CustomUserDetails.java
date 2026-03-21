package com.chis.trugarden.infrastructure.security;

import com.chis.trugarden.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;

public class CustomUserDetails implements UserDetails, Principal {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public static CustomUserDetails from(User user) {
        return new CustomUserDetails(user);
    }

    @Override
    public String getName() {
        return user.getEmail().value();
    }

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
}
