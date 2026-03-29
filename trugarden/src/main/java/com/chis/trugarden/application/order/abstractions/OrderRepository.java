package com.chis.trugarden.application.order.abstractions;

import com.chis.trugarden.domain.order.Order;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository {
    Optional<Order> findById(Long id);
    Order save(Order order);
}
