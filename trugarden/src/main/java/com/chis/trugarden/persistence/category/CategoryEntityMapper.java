package com.chis.trugarden.persistence.category;

import com.chis.trugarden.domain.category.Category;
import com.chis.trugarden.persistence.category.entities.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {
    default CategoryEntity toEntity(Category category) {
        if (category == null) {
            return null;
        }
        CategoryEntity entity = new CategoryEntity();

        if (category.getParentCategory() != null) {
            entity.setParentCategory(toEntity(category.getParentCategory()));
        } else {
            entity.setParentCategory(null);
        }
        entity.setId(category.getId());
        entity.setName(category.getName());
        entity.setCategoryCode(category.getCategoryCode());
        entity.setLevel(category.getLevel());
        return entity;
    }

    default Category toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getCategoryCode(),
                entity.getParentCategory() != null ? toDomain(entity.getParentCategory()) : null,
                entity.getLevel()
        );
    }
}
