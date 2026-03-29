package com.chis.trugarden.persistence.cart;

import com.chis.trugarden.persistence.cart.entities.CartEntity;
import com.chis.trugarden.shared.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByUserIdAndStatus(Long userId, CartStatus status);
    Optional<CartEntity> findBySessionIdAndStatus(String sessionId, CartStatus status);
}
