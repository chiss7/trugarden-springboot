package com.chis.trugarden.persistence.coupon.entities;

import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class CouponEntity extends BaseEntity {
    private String code;

    private double discountPercentage;

    private LocalDate startDate;
    private LocalDate endDate;

    private double minimumPurchaseAmount;
    private boolean active;

    @OneToMany(mappedBy = "usedCoupons")
    private Set<UserEntity> usedByUsers = new java.util.HashSet<>();
}
