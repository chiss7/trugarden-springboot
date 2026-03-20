package com.chis.trugarden.persistence.product.entities;

import com.chis.trugarden.persistence.user.entities.UserEntity;
import com.chis.trugarden.shared.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class ReviewEntity extends BaseEntity {

    @Column(nullable = false)
    private String reviewText;

    @Column(nullable = false)
    private double rating;

    @ElementCollection
    private List<String> images;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;
}
