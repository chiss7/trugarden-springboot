package com.chis.trugarden.auth;

import com.chis.trugarden.role.Role;
import com.chis.trugarden.role.RoleRepository;
import com.chis.trugarden.user.User;
import com.chis.trugarden.user.UserMapper;
import com.chis.trugarden.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public void register(RegistrationRequest request) {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER was not initialized"));
        User user = mapper.toUser(request, userRole);
        userRepository.save(user);
        // todo: send validation email
    }
}
