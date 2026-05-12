package com.chis.trugarden.persistence.product.entities;

import com.chis.trugarden.persistence.category.entities.CategoryEntity;
import com.chis.trugarden.shared.entity.BaseEntity;
import com.chis.trugarden.shared.enums.ProductType;
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

    @ElementCollection
    private List<String> images = new java.util.ArrayList<>();

    private BigDecimal originalPrice;
    private BigDecimal unitPrice;
    private double discountPercentage;
    private double stock;
    private int leadTimeMinDays;
    private int leadTimeMaxDays;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    private boolean hasIva;
    private int ivaPercentage;
    private int numRatings;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ReviewEntity> reviews = new java.util.ArrayList<>();
}
