package com.chis.trugarden.domain.stock;

import com.chis.trugarden.shared.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class StockReservation {
    private final Long id;
    private final Long productId;
    private final String productName;
    private final Long orderId;
    private final int quantity;
    private final ReservationStatus status;
    private final LocalDateTime expiresAt;
    private final LocalDateTime createdAt;

    public StockReservation(
            Long id,
            Long productId,
            String productName,
            Long orderId,
            int quantity,
            ReservationStatus status,
            LocalDateTime expiresAt,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.productId = Objects.requireNonNull(productId, "productId cannot be null");
        this.productName = Objects.requireNonNull(productName, "productName cannot be null");
        this.orderId = Objects.requireNonNull(orderId, "orderId cannot be null");
        this.quantity = quantity;
        this.status = Objects.requireNonNull(status, "status cannot be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt cannot be null");
        this.createdAt = createdAt;
    }

    public static StockReservation of(
            Long id,
            Long productId,
            String productName,
            Long orderId,
            int quantity,
            ReservationStatus status,
            LocalDateTime expiresAt,
            LocalDateTime createdAt
    ) {
        return new StockReservation(
                id,
                productId,
                productName,
                orderId,
                quantity,
                status,
                expiresAt,
                createdAt
        );
    }

    public static StockReservation ofNew(
            Long productId,
            String productName,
            Long orderId,
            int quantity,
            LocalDateTime expiresAt
    ) {
        return new StockReservation(
                null,
                productId,
                productName,
                orderId,
                quantity,
                ReservationStatus.ACTIVE,
                expiresAt,
                LocalDateTime.now()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public StockReservation withStatus(ReservationStatus newStatus) {
        return new StockReservation(
                this.id,
                this.productId,
                this.productName,
                this.orderId,
                this.quantity,
                newStatus,
                this.expiresAt,
                this.createdAt
        );
    }

    public boolean isActive() {
        return status == ReservationStatus.ACTIVE;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt) && status == ReservationStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return "StockReservation{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", orderId=" + orderId +
                ", quantity=" + quantity +
                ", status=" + status +
                ", expiresAt=" + expiresAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
