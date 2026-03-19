package com.chis.trugarden.persistence.user;

import com.chis.trugarden.application.auth.register.RegisterCommand;
import com.chis.trugarden.persistence.role.entities.RoleEntity;
import com.chis.trugarden.persistence.user.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserEntity toUser(RegisterCommand command, RoleEntity roleEntity) {
        return UserEntity.builder()
                .firstname(command.firstname())
                .lastname(command.lastname())
                .email(command.email())
                .password(passwordEncoder.encode(command.password()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(roleEntity))
                .build();
    }
}
