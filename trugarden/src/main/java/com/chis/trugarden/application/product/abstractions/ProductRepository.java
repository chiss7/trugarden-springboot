package com.chis.trugarden.application.product.abstractions;

import com.chis.trugarden.domain.product.Product;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface ProductRepository {
    Product save(Product product);
}
