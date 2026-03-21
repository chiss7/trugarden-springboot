package com.chis.trugarden.application.user.abstractions;

import com.chis.trugarden.domain.user.User;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
