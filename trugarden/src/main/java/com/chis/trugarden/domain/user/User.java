package com.chis.trugarden.domain.user;

import com.chis.trugarden.domain.role.Role;
import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import com.chis.trugarden.shared.enums.Roles;

import java.time.LocalDate;
import java.util.Collections;
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
            Set<Role> roles
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
            Set<Role> roles
    ) {
        return new User(id, firstname, lastname, dateOfBirth, image,
                email, password, accountLocked, enabled, roles);
    }

    public static User ofNew(
            String firstname,
            String lastname,
            LocalDate dateOfBirth,
            String image,
            Email email,
            Password password,
            Set<Role> initialRoles
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
                initialRoles != null ? initialRoles : Set.of(Role.ofNew(Roles.ROLE_CUSTOMER))
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
                this.roles
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
                this.roles
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
                this.roles
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
                '}';
    }
}
