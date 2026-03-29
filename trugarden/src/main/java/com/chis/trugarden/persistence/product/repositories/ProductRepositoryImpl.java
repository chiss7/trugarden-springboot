package com.chis.trugarden.persistence.product.repositories;

import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.persistence.product.ProductEntityMapper;
import com.chis.trugarden.persistence.product.ProductJpaRepository;
import com.chis.trugarden.persistence.product.entities.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = productEntityMapper.toEntity(product);
        return productEntityMapper.toDomain(productJpaRepository.save(entity));
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id)
                .map(productEntityMapper::toDomain)
                .or(() -> {
                    log.warn("Product with id {} not found", id);
                    return Optional.empty();
                });
    }

    @Override
    public Optional<Product> findBySlug(String slug) {
        return productJpaRepository.findBySlug(slug)
                .map(productEntityMapper::toDomain)
                .or(() -> {
                    log.warn("Product with slug {} not found", slug);
                    return Optional.empty();
                });
    }

    @Override
    public Optional<Product> findByIdWithLock(Long id) {
        return productJpaRepository.findByIdWithLock(id)
                .map(productEntityMapper::toDomain)
                .or(() -> {
                    log.warn("Product with id {} not found (fetched with lock)", id);
                    return Optional.empty();
                });
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        List<ProductEntity> entities = products.stream()
                .map(productEntityMapper::toEntity)
                .toList();
        List<ProductEntity> savedEntities = productJpaRepository.saveAll(entities);
        return savedEntities.stream()
                .map(productEntityMapper::toDomain)
                .toList();
    }
}
