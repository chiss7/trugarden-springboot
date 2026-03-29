package com.chis.trugarden.persistence.user;

import com.chis.trugarden.domain.role.Role;
import com.chis.trugarden.domain.user.Address;
import com.chis.trugarden.domain.user.Email;
import com.chis.trugarden.domain.user.Password;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.persistence.role.RoleEntityMapper;
import com.chis.trugarden.persistence.role.entities.RoleEntity;
import com.chis.trugarden.persistence.user.entities.AddressEntity;
import com.chis.trugarden.persistence.user.entities.UserEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    RoleEntityMapper roleEntityMapper = Mappers.getMapper(RoleEntityMapper.class);
    AddressEntityMapper addressEntityMapper = Mappers.getMapper(AddressEntityMapper.class);

    default User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        Set<Role> roles = entity.getRoles()
                .stream()
                .map(roleEntityMapper::toDomain)
                .collect(Collectors.toSet());

        List<Address> addresses = entity.getAddresses()
                .stream()
                .map(addressEntityMapper::toDomain)
                .toList();

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
                roles,
                addresses,
                entity.getGoogleId(),
                entity.getAuthProvider()
        );
    }

    default UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        List<RoleEntity> roleEntities = domain.getRoles().stream()
                .map(roleEntityMapper::toEntity)
                .toList();

        if (domain.getAddresses() != null && !domain.getAddresses().isEmpty()) {
            entity.setAddresses(
                    domain.getAddresses().stream()
                            .map(addressEntityMapper::toEntity)
                            .toList()
            );
        } else {
            entity.setAddresses(List.of());
        }

        entity.setId(domain.getId());
        entity.setFirstname(domain.getFirstname());
        entity.setLastname(domain.getLastname());
        entity.setDateOfBirth(domain.getDateOfBirth());
        entity.setImage(domain.getImage());
        entity.setEmail(domain.getEmail().value());
        entity.setPassword(domain.getPassword() != null ? domain.getPassword().hashedValue() : null);
        entity.setAccountLocked(domain.isAccountLocked());
        entity.setEnabled(domain.isEnabled());
        entity.setRoles(roleEntities);
        entity.setGoogleId(domain.getGoogleId());
        entity.setAuthProvider(domain.getAuthProvider());

        return entity;
    }
}