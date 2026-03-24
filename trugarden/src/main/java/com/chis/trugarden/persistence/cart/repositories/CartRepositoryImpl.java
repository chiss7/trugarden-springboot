package com.chis.trugarden.persistence.cart.repositories;

import com.chis.trugarden.application.cart.abstractions.CartRepository;
import com.chis.trugarden.domain.cart.Cart;
import com.chis.trugarden.persistence.cart.CartEntityMapper;
import com.chis.trugarden.persistence.cart.CartJpaRepository;
import com.chis.trugarden.persistence.cart.entities.CartEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {
    private final CartJpaRepository cartJpaRepository;
    private final CartEntityMapper cartEntityMapper;

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return cartJpaRepository.findByUserId(userId)
                .map(cartEntityMapper::toDomain)
                .or(() -> {
                    log.warn("No cart found for userId {}", userId);
                    return Optional.empty();
                });
    }

    @Override
    public Optional<Cart> findBySessionId(String sessionId) {
        return cartJpaRepository.findBySessionId(sessionId)
                .map(cartEntityMapper::toDomain)
                .or(() -> {
                    log.warn("No cart found for sessionId {}", sessionId);
                    return Optional.empty();
                });
    }

    @Override
    public Cart save(Cart cart) {
        CartEntity entity = cartEntityMapper.toEntity(cart);
        return cartEntityMapper.toDomain(cartJpaRepository.save(entity));
    }
}
