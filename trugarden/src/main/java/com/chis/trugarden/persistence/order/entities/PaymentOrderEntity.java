package com.chis.trugarden.persistence.order.entities;

import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.entity.BaseEntity;
import com.chis.trugarden.shared.enums.PaymentMethod;
import com.chis.trugarden.shared.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_order")
public class PaymentOrderEntity extends BaseEntity {
    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String paymentLinkId;

    @ManyToOne
    private UserEntity user;

    // INNECESARIO
    @OneToMany
    private Set<OrderEntity> orders = new java.util.HashSet<>();
}
