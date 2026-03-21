package com.chis.trugarden.persistence.role;

import com.chis.trugarden.domain.role.Role;
import com.chis.trugarden.persistence.role.entities.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleEntityMapper {
    default RoleEntity toEntity(Role role) {
        if (role == null) {
            return null;
        }
        RoleEntity entity = new RoleEntity();
        entity.setId(role.getId());
        entity.setName(role.getName());
        return entity;
    }

    default Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return Role.of(
                entity.getId(),
                entity.getName()
        );
    }
}
