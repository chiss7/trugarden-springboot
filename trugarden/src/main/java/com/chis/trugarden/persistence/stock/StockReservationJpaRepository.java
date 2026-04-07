package com.chis.trugarden.persistence.stock;

import com.chis.trugarden.persistence.stock.entities.StockReservationEntity;
import com.chis.trugarden.shared.enums.ReservationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockReservationJpaRepository extends JpaRepository<StockReservationEntity, Long> {

    /**
     * Calcula el total de unidades reservadas activamente para un producto.
     */
    @Query("SELECT COALESCE(SUM(r.quantity), 0) FROM StockReservationEntity r " +
           "WHERE r.productId = :productId AND r.status = 'ACTIVE'")
    int sumActiveReservationsByProductId(@Param("productId") Long productId);

    /**
     * Encuentra todas las reservas expiradas (activas y con fecha de expiración pasada).
     */
    @Query("SELECT r FROM StockReservationEntity r " +
           "WHERE r.status = 'ACTIVE' AND r.expiresAt < :now")
    List<StockReservationEntity> findExpiredReservations(@Param("now") LocalDateTime now);

    /**
     * Encuentra reservas por orderId y status.
     */
    List<StockReservationEntity> findByOrderIdAndStatus(Long orderId, ReservationStatus status);

    /**
     * Encuentra reservas activas por orderId con lock pesimista.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM StockReservationEntity r WHERE r.orderId = :orderId AND r.status = 'ACTIVE'")
    List<StockReservationEntity> findActiveByOrderIdWithLock(@Param("orderId") Long orderId);

    /**
     * Encuentra todas las reservas por orderId (cualquier status).
     */
    List<StockReservationEntity> findByOrderId(Long orderId);
}
