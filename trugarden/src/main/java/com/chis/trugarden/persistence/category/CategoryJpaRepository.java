package com.chis.trugarden.persistence.category;

import com.chis.trugarden.persistence.category.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByCategoryCode(String code);
}
