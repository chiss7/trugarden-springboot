package com.chis.trugarden.persistence.role.entities;

import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles") // mappedBy = same variable 'roles' in User class
    @JsonIgnore // ignore the users field when serializing the object into JSON or deserializing JSON into the object.
    // JsonIgnore is particularly useful to avoid issues like circular references.
    // Without JsonIgnore, when converting a Role object to JSON, it could keep serializing the User objects inside
    // the Role, which also contain Role objects, and so on—causing an infinite loop.
    private List<UserEntity> users;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
