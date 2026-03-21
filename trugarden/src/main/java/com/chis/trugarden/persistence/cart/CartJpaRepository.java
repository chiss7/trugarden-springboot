package com.chis.trugarden.persistence.cart;

import com.chis.trugarden.persistence.cart.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {
}
