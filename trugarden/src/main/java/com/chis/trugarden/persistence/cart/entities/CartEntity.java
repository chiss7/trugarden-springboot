package com.chis.trugarden.persistence.cart.entities;

import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class CartEntity extends BaseEntity {
    @OneToOne
    private UserEntity user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private Set<CartItemEntity> cartItems = new java.util.HashSet<>();

    private double totalPrice;

    private int totalMrpPrice;

    private int quantity;

    private int discount;

    private String couponCode;
}
