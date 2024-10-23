package com.chis.trugarden.user;

import com.chis.trugarden.order.Order;
import com.chis.trugarden.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {
    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String image;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean accountLocked;
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER) // when the entity is loaded, the roles list will also be loaded immediately
    private List<Role> roles;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @Override
    public String getName() {
        return email; // this is the unique identifier of our user
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // Returns true if the user's account is valid (ie non-expired), false if no longer valid (ie expired)
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired(); // true
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    // Indicates whether the user's credentials (password) has expired. Expired credentials prevent authentication.
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired(); // true
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }
}
