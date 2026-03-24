package com.chis.trugarden.persistence.cart.entities;

import com.chis.trugarden.persistence.product.entities.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cart_item")
public class CartItemEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @OneToOne
    private ProductEntity product;

    private int quantity;

    private BigDecimal mrpPrice;

    private BigDecimal sellingPrice;

    private Long userId;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
