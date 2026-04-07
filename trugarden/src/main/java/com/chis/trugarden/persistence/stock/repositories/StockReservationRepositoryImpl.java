package com.chis.trugarden.persistence.stock.repositories;

import com.chis.trugarden.application.stock.abstractions.StockReservationRepository;
import com.chis.trugarden.domain.stock.StockReservation;
import com.chis.trugarden.persistence.stock.StockReservationJpaRepository;
import com.chis.trugarden.persistence.stock.StockReservationMapper;
import com.chis.trugarden.shared.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockReservationRepositoryImpl implements StockReservationRepository {

    private final StockReservationJpaRepository jpaRepository;
    private final StockReservationMapper mapper;

    @Override
    public StockReservation save(StockReservation reservation) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(reservation)));
    }

    @Override
    public List<StockReservation> saveAll(List<StockReservation> reservations) {
        return jpaRepository.saveAll(
                reservations.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        ).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int sumActiveReservationsByProductId(Long productId) {
        return jpaRepository.sumActiveReservationsByProductId(productId);
    }

    @Override
    public List<StockReservation> findExpiredReservations(LocalDateTime now) {
        return jpaRepository.findExpiredReservations(now).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockReservation> findByOrderIdAndStatus(Long orderId, ReservationStatus status) {
        return jpaRepository.findByOrderIdAndStatus(orderId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockReservation> findActiveByOrderIdWithLock(Long orderId) {
        return jpaRepository.findActiveByOrderIdWithLock(orderId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockReservation> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
