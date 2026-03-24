package com.chis.trugarden.persistence.user.repositories;

import com.chis.trugarden.application.auth.abstractions.TokenRepository;
import com.chis.trugarden.domain.user.Token;
import com.chis.trugarden.persistence.user.TokenEntityMapper;
import com.chis.trugarden.persistence.user.TokenJpaRepository;
import com.chis.trugarden.persistence.user.entities.TokenEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {
    private final TokenJpaRepository tokenJpaRepository;
    private final TokenEntityMapper tokenEntityMapper;

    @Override
    public Token save(Token token) {
        TokenEntity entity = tokenEntityMapper.toEntity(token);
        return tokenEntityMapper.toShallowDomain(tokenJpaRepository.save(entity));
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenJpaRepository.findByToken(token)
                .map(tokenEntityMapper::toDomain)
                .or(() -> {
                    log.warn("Token not found for token: {}", token);
                    return Optional.empty();
                });
    }
}
