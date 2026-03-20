package com.chis.trugarden.persistence.cart.entities;

import com.chis.trugarden.persistence.product.entities.ProductEntity;
import com.chis.trugarden.shared.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItemEntity extends BaseEntity {

    @ManyToOne
    @JsonIgnore
    private CartEntity cart;

    @OneToOne
    private ProductEntity product;

    private int quantity = 1;

    private Integer mrpPrice;

    private double sellingPrice;

    private Long userId;

}
