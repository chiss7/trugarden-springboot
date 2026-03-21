package com.chis.trugarden.application.auth.abstractions;

import com.chis.trugarden.domain.user.Token;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository {
    Token save(Token token);
    Optional<Token> findByToken(String token);
}
