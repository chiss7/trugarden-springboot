package com.chis.trugarden.persistence.order.entities;

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
@Builder
@Getter
@Setter
@Entity
@Table(name = "order_item")
@EntityListeners(AuditingEntityListener.class)
public class OrderItemEntity {
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    private OrderEntity order;

    @ManyToOne
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
