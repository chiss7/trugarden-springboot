package com.chis.trugarden.persistence.order.repositories;

import com.chis.trugarden.application.order.abstractions.OrderRepository;
import com.chis.trugarden.domain.order.Order;
import com.chis.trugarden.persistence.order.OrderEntityMapper;
import com.chis.trugarden.persistence.order.OrderJpaRepository;
import com.chis.trugarden.persistence.order.entities.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderEntityMapper orderEntityMapper;
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id)
                .map(orderEntityMapper::toDomain)
                .or(() -> {
                    log.warn("Order with id {} not found", id);
                    return Optional.empty();
                });
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(order);
        return orderEntityMapper.toDomain(orderJpaRepository.save(orderEntity));
    }

    @Override
    public Optional<Order> findByOrderId(String orderId) {
        return orderJpaRepository.findByOrderId(orderId)
                .map(orderEntityMapper::toDomain)
                .or(() -> {
                    log.warn("Order with orderId {} not found", orderId);
                    return Optional.empty();
                });
    }
}
