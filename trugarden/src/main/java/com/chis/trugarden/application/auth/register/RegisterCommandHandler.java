package com.chis.trugarden.application.auth.register;

import com.chis.trugarden.application.auth.service.SendValidationTokenService;
import com.chis.trugarden.application.role.abstractions.RoleRepository;
import com.chis.trugarden.application.user.abstractions.UserRepository;
import com.chis.trugarden.domain.role.Role;
import com.chis.trugarden.domain.role.RoleErrors;
import com.chis.trugarden.domain.user.*;
import com.chis.trugarden.shared.enums.Roles;
import com.chis.trugarden.shared.exception.PasswordMismatchException;
import com.chis.trugarden.shared.result.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterCommandHandler {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendValidationTokenService sendValidationTokenService;

    @CommandHandler
    @Transactional
    public Result<Long> handle(RegisterCommand command) {
        log.info("Creating new customer with email {}", command.email());
        Optional<Role> roleOpt = roleRepository.findByName(Roles.ROLE_CUSTOMER);
        if (roleOpt.isEmpty()) {
            return Result.failure(RoleErrors.notFound(Roles.ROLE_CUSTOMER));
        }

        Optional<User> existingUserOpt = userRepository.findByEmail(command.email());
        if (existingUserOpt.isPresent()) {
            log.info("User with email {} already exists", command.email());
            return Result.failure(UserErrors.alreadyExists(command.email()));
        }

        if(!command.password().equals(command.confirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }

        User user = User.ofNew(
                command.firstname(),
                command.lastname(),
                null,
                null,
                Email.of(command.email()),
                Password.ofHashed(passwordEncoder.encode(command.password())),
                Set.of(roleOpt.get()),
                null
        );

        User savedUser = userRepository.save(user);
        sendValidationTokenService.sendValidationEmail(savedUser);
        return Result.success(savedUser.getId());
    }
}
