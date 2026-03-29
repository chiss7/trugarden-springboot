package com.chis.trugarden.application.cart.abstractions;

import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.shared.enums.CartStatus;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface CartRepository {
    Optional<Cart> findByUserIdAndStatus(Long userId, CartStatus status);
    Optional<Cart> findBySessionIdAndStatus(String sessionId, CartStatus status);
    Cart save(Cart cart);
}
