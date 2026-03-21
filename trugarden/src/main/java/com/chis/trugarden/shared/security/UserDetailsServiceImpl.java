package com.chis.trugarden.shared.security;

import com.chis.trugarden.domain.user.User;
import com.chis.trugarden.infrastructure.security.CustomUserDetails;
import com.chis.trugarden.persistence.user.UserEntityMapper;
import com.chis.trugarden.persistence.user.UserJpaRepository;
import com.chis.trugarden.persistence.user.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserJpaRepository repository;
    private final UserEntityMapper userEntityMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserEntity entity = repository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        User domainUser = userEntityMapper.toDomain(entity);

        return CustomUserDetails.from(domainUser);
    }
}
