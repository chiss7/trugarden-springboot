package com.chis.trugarden.persistence.order;

import com.chis.trugarden.persistence.order.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
