package com.chis.trugarden.persistence.cart;

import com.chis.trugarden.persistence.cart.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByUserId(Long userId);
    Optional<CartEntity> findBySessionId(String sessionId);
}
