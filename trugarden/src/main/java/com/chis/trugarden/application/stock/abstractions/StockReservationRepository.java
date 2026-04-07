package com.chis.trugarden.application.stock.abstractions;

import com.chis.trugarden.domain.stock.StockReservation;
import com.chis.trugarden.shared.enums.ReservationStatus;
import org.jmolecules.ddd.annotation.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockReservationRepository {

    /**
     * Guarda una reserva de stock.
     */
    StockReservation save(StockReservation reservation);

    /**
     * Guarda múltiples reservas de stock.
     */
    List<StockReservation> saveAll(List<StockReservation> reservations);

    /**
     * Calcula el total de unidades reservadas activamente para un producto.
     */
    int sumActiveReservationsByProductId(Long productId);

    /**
     * Encuentra todas las reservas expiradas (activas y con fecha de expiración pasada).
     */
    List<StockReservation> findExpiredReservations(LocalDateTime now);

    /**
     * Encuentra reservas por orderId y status.
     */
    List<StockReservation> findByOrderIdAndStatus(Long orderId, ReservationStatus status);

    /**
     * Encuentra reservas activas por orderId con lock pesimista.
     */
    List<StockReservation> findActiveByOrderIdWithLock(Long orderId);

    /**
     * Encuentra todas las reservas por orderId (cualquier status).
     */
    List<StockReservation> findByOrderId(Long orderId);
}
