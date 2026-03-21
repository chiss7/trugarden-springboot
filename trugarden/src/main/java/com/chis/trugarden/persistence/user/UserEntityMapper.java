package com.chis.trugarden.persistence.user;

import com.chis.trugarden.domain.role.Role;
import com.chis.trugarden.domain.user.Email;
import com.chis.trugarden.domain.user.Password;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.persistence.role.entities.RoleEntity;
import com.chis.trugarden.persistence.user.entities.UserEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    default User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        Set<Role> roles = entity.getRoles()
                .stream()
                .map(roleEntity -> Role.of(roleEntity.getId(), roleEntity.getName()))
                .collect(Collectors.toSet());

        return User.of(
                entity.getId(),
                entity.getFirstname(),
                entity.getLastname(),
                entity.getDateOfBirth(),
                entity.getImage(),
                Email.of(entity.getEmail()),
                Password.ofHashed(entity.getPassword()),
                entity.isAccountLocked(),
                entity.isEnabled(),
                roles
        );
    }

    default UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        List<RoleEntity> roleEntities = domain.getRoles().stream()
                .map(role -> {
                    RoleEntity roleEntity = new RoleEntity();
                    roleEntity.setId(role.getId());
                    roleEntity.setName(role.getName());
                    return roleEntity;
                })
                .toList();

        entity.setId(domain.getId());
        entity.setFirstname(domain.getFirstname());
        entity.setLastname(domain.getLastname());
        entity.setDateOfBirth(domain.getDateOfBirth());
        entity.setImage(domain.getImage());
        entity.setEmail(domain.getEmail().value());
        entity.setPassword(domain.getPassword().hashedValue());
        entity.setAccountLocked(domain.isAccountLocked());
        entity.setEnabled(domain.isEnabled());
        entity.setRoles(roleEntities);

        return entity;
    }
}