package com.chis.trugarden.persistence.category.repositories;

import com.chis.trugarden.application.category.abstractions.CategoryRepository;
import com.chis.trugarden.domain.category.Category;
import com.chis.trugarden.persistence.category.CategoryEntityMapper;
import com.chis.trugarden.persistence.category.CategoryJpaRepository;
import com.chis.trugarden.persistence.category.entities.CategoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = categoryEntityMapper.toEntity(category);
        return categoryEntityMapper.toDomain(categoryJpaRepository.save(entity));
    }

    @Override
    public Optional<Category> findByCode(String code) {
        return categoryJpaRepository.findByCategoryCode(code)
                .map(categoryEntityMapper::toDomain)
                .or(() -> {
                    log.warn("No se encontró la categoría con código: {}", code);
                    return Optional.empty();
                });
    }

    @Override
    public List<Category> findByLevel(Integer level) {
        List<CategoryEntity> categoryEntities = categoryJpaRepository.findByLevel(level);
        return categoryEntities.stream().map(categoryEntityMapper::toDomain).toList();
    }
}
