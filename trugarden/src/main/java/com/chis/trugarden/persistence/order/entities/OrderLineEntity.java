package com.chis.trugarden.persistence.order.entities;

import com.chis.trugarden.persistence.product.entities.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "order_line")
public class OrderLineEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
