package com.chis.trugarden.domain.user;

import com.chis.trugarden.domain.role.Role;
import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import com.chis.trugarden.shared.enums.AuthProvider;
import com.chis.trugarden.shared.enums.Roles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class User {

    private final Long id;
    private final String firstname;
    private final String lastname;
    private final LocalDate dateOfBirth;
    private final String image;
    private final Email email;
    private final Password password;
    private final boolean accountLocked;
    private final boolean enabled;
    private final Set<Role> roles;
    private final List<Address> addresses;
    private final String googleId;
    private final AuthProvider authProvider;

    public User(
            Long id,
            String firstname,
            String lastname,
            LocalDate dateOfBirth,
            String image,
            Email email,
            Password password,
            boolean accountLocked,
            boolean enabled,
            Set<Role> roles,
            List<Address> addresses,
            String googleId,
            AuthProvider authProvider
    ) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.image = image;
        this.email = email;
        this.password = password;
        this.accountLocked = accountLocked;
        this.enabled = enabled;
        this.roles = roles != null
                ? Set.copyOf(roles)
                : Collections.emptySet();
        this.addresses = addresses;
        this.googleId = googleId;
        this.authProvider = authProvider;
    }

    public static User of(
            Long id,
            String firstname,
            String lastname,
            LocalDate dateOfBirth,
            String image,
            Email email,
            Password password,
            boolean accountLocked,
            boolean enabled,
            Set<Role> roles,
            List<Address> addresses,
            String googleId,
            AuthProvider authProvider
    ) {
        return new User(id, firstname, lastname, dateOfBirth, image,
                email, password, accountLocked, enabled, roles, addresses, googleId, authProvider);
    }

    public static User ofNew(
            String firstname,
            String lastname,
            LocalDate dateOfBirth,
            String image,
            Email email,
            Password password,
            Set<Role> initialRoles,
            List<Address> addresses
    ) {
        return new User(
                null,
                firstname,
                lastname,
                dateOfBirth,
                image,
                email,
                password,
                false,
                false,
                initialRoles != null ? initialRoles : Set.of(Role.ofNew(Roles.ROLE_CUSTOMER)),
                addresses,
                null,
                AuthProvider.LOCAL
        );
    }

    public static User ofNewOAuth(
            String firstname,
            String lastname,
            String image,
            Email email,
            String googleId,
            Set<Role> initialRoles,
            List<Address> addresses
    ) {
        return new User(
                null,
                firstname,
                lastname,
                null,
                image,
                email,
                null,
                false,
                true,
                initialRoles != null ? initialRoles : Set.of(Role.ofNew(Roles.ROLE_CUSTOMER)),
                addresses,
                googleId,
                AuthProvider.GOOGLE
        );
    }

    public User withLockedAccount() {
        return new User(
                this.id,
                this.firstname,
                this.lastname,
                this.dateOfBirth,
                this.image,
                this.email,
                this.password,
                true,
                this.enabled,
                this.roles,
                this.addresses,
                this.googleId,
                this.authProvider
        );
    }

    public User withUnlockedAccount() {
        return new User(
                this.id,
                this.firstname,
                this.lastname,
                this.dateOfBirth,
                this.image,
                this.email,
                this.password,
                false,
                this.enabled,
                this.roles,
                this.addresses,
                this.googleId,
                this.authProvider
        );
    }

    public User withEnabled(boolean enabled) {
        return new User(
                this.id,
                this.firstname,
                this.lastname,
                this.dateOfBirth,
                this.image,
                this.email,
                this.password,
                this.accountLocked,
                enabled,
                this.roles,
                this.addresses,
                this.googleId,
                this.authProvider
        );
    }

    public User withNewAddress(Address newAddress) {
        List<Address> updatedAddresses = this.addresses != null
                ? new java.util.ArrayList<>(this.addresses)
                : new java.util.ArrayList<>();
        updatedAddresses.add(newAddress);
        return new User(
                this.id,
                this.firstname,
                this.lastname,
                this.dateOfBirth,
                this.image,
                this.email,
                this.password,
                this.accountLocked,
                this.enabled,
                this.roles,
                updatedAddresses,
                this.googleId,
                this.authProvider
        );
    }

    public User withGoogleId(String googleId) {
        return new User(
                this.id,
                this.firstname,
                this.lastname,
                this.dateOfBirth,
                this.image,
                this.email,
                this.password,
                this.accountLocked,
                this.enabled,
                this.roles,
                this.addresses,
                googleId,
                this.authProvider
        );
    }

    public CustomUserDetails toPrincipal() {
        return new CustomUserDetails(this);
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getImage() {
        return image;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public String getGoogleId() {
        return googleId;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public boolean hasCreatedAddress() {
        return addresses != null && !addresses.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email=" + email +
                ", accountLocked=" + accountLocked +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", addresses=" + addresses +
                '}';
    }
}
