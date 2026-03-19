package com.chis.trugarden.persistence.product.entities;

import com.chis.trugarden.persistence.category.entities.CategoryEntity;
import com.chis.trugarden.shared.entity.BaseEntity;
import com.chis.trugarden.persistence.order.entities.OrderLineEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity {
    private String name;
    @Column(unique = true)
    private String slug;
    private String description;
    private String image;
    private BigDecimal price;
    private Double stock;
    private boolean hasIva;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "product")
    private List<OrderLineEntity> orderLineEntities;
}
