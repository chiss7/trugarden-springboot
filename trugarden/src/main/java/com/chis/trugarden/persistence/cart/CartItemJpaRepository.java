package com.chis.trugarden.persistence.cart;

import com.chis.trugarden.persistence.cart.entities.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {
}
