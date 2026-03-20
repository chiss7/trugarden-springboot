package com.chis.trugarden.persistence.product;

import com.chis.trugarden.persistence.product.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
