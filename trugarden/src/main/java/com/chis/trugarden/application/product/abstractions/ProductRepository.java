package com.chis.trugarden.application.product.abstractions;

import com.chis.trugarden.domain.product.Product;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Optional<Product> findBySlug(String slug);
}
