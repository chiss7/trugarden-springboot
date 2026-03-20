package com.chis.trugarden.persistence.category.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @NotNull
    @Column(unique = true)
    private String categoryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity parentCategory;

    @NotNull
    private Integer level;
}
