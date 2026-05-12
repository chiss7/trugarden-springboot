package com.chis.trugarden.persistence.user.entities;

import com.chis.trugarden.persistence.coupon.entities.CouponEntity;
import com.chis.trugarden.persistence.order.entities.OrderEntity;
import com.chis.trugarden.persistence.role.entities.RoleEntity;
import com.chis.trugarden.shared.enums.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String googleId;
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressEntity> addresses;

    @ManyToMany(fetch = FetchType.EAGER) // when the entity is loaded, the roles list will also be loaded immediately
    @JoinTable(
            name = "_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private List<RoleEntity> roles;

    @OneToMany(mappedBy = "user")
    private List<OrderEntity> orders;

    @OneToMany
    @JsonIgnore
    private Set<CouponEntity> usedCoupons = new java.util.HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }
}
