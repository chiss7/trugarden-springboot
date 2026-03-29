package com.chis.trugarden.infrastructure.security;

import com.chis.trugarden.application.role.abstractions.RoleRepository;
import com.chis.trugarden.application.user.abstractions.UserRepository;
import com.chis.trugarden.domain.role.Role;
import com.chis.trugarden.domain.user.Email;
import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.shared.enums.Roles;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends OidcUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String googleId  = oidcUser.getAttribute("sub");
        String email     = oidcUser.getAttribute("email");
        String firstname = oidcUser.getAttribute("given_name");
        String lastname  = oidcUser.getAttribute("family_name");
        String image     = oidcUser.getAttribute("picture");

        Optional<User> userByGoogleId = userRepository.findByGoogleId(googleId);
        if (userByGoogleId.isPresent()) {
            return CustomUserDetails.from(userByGoogleId.get(), oidcUser);
        }

        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            User saved = userRepository.save(userByEmail.get().withGoogleId(googleId));
            return CustomUserDetails.from(saved, oidcUser);
        }

        Role customerRole = roleRepository.findByName(Roles.ROLE_CUSTOMER)
                .orElseThrow(() -> new RuntimeException("ROLE_CUSTOMER not found in database"));

        User newUser = User.ofNewOAuth(
                firstname != null ? firstname : "",
                lastname != null ? lastname : "",
                image,
                Email.of(email),
                googleId,
                Set.of(customerRole),
                null
        );
        return CustomUserDetails.from(userRepository.save(newUser), oidcUser);
    }
}