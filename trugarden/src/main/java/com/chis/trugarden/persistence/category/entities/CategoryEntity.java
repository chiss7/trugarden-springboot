package com.chis.trugarden.persistence.category.entities;

import com.chis.trugarden.persistence.product.entities.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<ProductEntity> productEntities;
}
