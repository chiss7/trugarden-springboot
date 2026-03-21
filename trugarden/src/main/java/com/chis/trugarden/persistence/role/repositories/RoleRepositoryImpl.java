package com.chis.trugarden.persistence.role.repositories;

import com.chis.trugarden.application.role.abstractions.RoleRepository;
import com.chis.trugarden.domain.role.Role;
import com.chis.trugarden.persistence.role.RoleEntityMapper;
import com.chis.trugarden.persistence.role.RoleJpaRepository;
import com.chis.trugarden.shared.enums.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleJpaRepository roleJpaRepository;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public Optional<Role> findByName(Roles name) {
        return roleJpaRepository.findByName(name)
                .map(roleEntityMapper::toDomain)
                .or(() -> {
                    log.warn("Role with name {} not found", name);
                    return Optional.empty();
                });
    }
}
