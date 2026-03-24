package com.chis.trugarden.application.cart.abstractions;

import com.chis.trugarden.domain.cart.Cart;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface CartRepository {
    Optional<Cart> findByUserId(Long userId);
    Optional<Cart> findBySessionId(String sessionId);
    Cart save(Cart cart);
}
