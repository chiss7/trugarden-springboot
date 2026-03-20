package com.chis.trugarden.persistence.role;

import com.chis.trugarden.persistence.role.entities.RoleEntity;
import com.chis.trugarden.shared.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(Roles name);
}
