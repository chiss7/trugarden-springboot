package com.chis.trugarden.persistence.stock.entities;

import com.chis.trugarden.shared.entity.BaseEntity;
import com.chis.trugarden.shared.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stock_reservation", indexes = {
        @Index(name = "idx_reservation_status_expires", columnList = "status, expiresAt"),
        @Index(name = "idx_reservation_order", columnList = "orderId"),
        @Index(name = "idx_reservation_product", columnList = "productId")
})
public class StockReservationEntity extends BaseEntity {
    
    @Column(nullable = false)
    private Long productId;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private Long orderId;
    
    @Column(nullable = false)
    private int quantity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
