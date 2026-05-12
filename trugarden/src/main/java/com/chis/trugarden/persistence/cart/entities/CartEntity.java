package com.chis.trugarden.persistence.cart.entities;

import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserEntity user;

    private String sessionId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItemEntity> cartItems = new java.util.HashSet<>();

    private BigDecimal subtotal;

    private BigDecimal totalTax;

    private BigDecimal discountPercentage;

    private BigDecimal couponDiscountAmount;

    private BigDecimal grandTotal;

    private int quantity;

    private String couponCode;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
