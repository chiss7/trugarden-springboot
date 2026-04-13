package com.chis.trugarden.application.category.abstractions;

import com.chis.trugarden.domain.category.Category;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findByCode(String code);
    List<Category> findByLevel(Integer level);
}
