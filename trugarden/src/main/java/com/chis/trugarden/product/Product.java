package com.chis.trugarden.product;

import com.chis.trugarden.category.Category;
import com.chis.trugarden.common.BaseEntity;
import com.chis.trugarden.orderline.OrderLine;
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
public class Product extends BaseEntity {
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
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderLine> orderLines;
}
