package com.chis.trugarden.persistence.product.repositories;

import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.persistence.category.CategoryEntityMapper;
import com.chis.trugarden.persistence.product.ProductEntityMapper;
import com.chis.trugarden.persistence.product.ProductJpaRepository;
import com.chis.trugarden.persistence.product.entities.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductEntityMapper productEntityMapper;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = productEntityMapper.toEntity(product);
        return productEntityMapper.toDomain(productJpaRepository.save(entity), categoryEntityMapper);
    }
}
