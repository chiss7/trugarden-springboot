package com.chis.trugarden.persistence.order.entities;

import com.chis.trugarden.persistence.user.entities.AddressEntity;
import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.enums.OrderStatus;
import com.chis.trugarden.shared.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String orderId;

    @ManyToOne
    private UserEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems = new java.util.ArrayList<>();

    @ManyToOne
    private AddressEntity shippingAddress;

    @Embedded
    private PaymentDetailsEntity paymentDetails = new PaymentDetailsEntity();

    private BigDecimal totalOriginalPrice;
    private BigDecimal subtotal;
    private BigDecimal totalTax;
    private BigDecimal discountPercentage;
    private BigDecimal couponDiscountAmount;
    private BigDecimal grandTotal;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
