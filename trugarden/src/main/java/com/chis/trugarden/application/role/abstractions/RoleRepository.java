package com.chis.trugarden.application.role.abstractions;

import com.chis.trugarden.domain.role.Role;
import com.chis.trugarden.shared.enums.Roles;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository {
    Optional<Role> findByName(Roles name);
}
