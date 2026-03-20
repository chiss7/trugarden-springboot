package com.chis.trugarden.persistence.product;

import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.persistence.category.CategoryEntityMapper;
import com.chis.trugarden.persistence.category.entities.CategoryEntity;
import com.chis.trugarden.persistence.product.entities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    default ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        ProductEntity entity = new ProductEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(product.getCategory().getId());
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setSlug(product.getSlug());
        entity.setDescription(product.getDescription());
        entity.setMrpPrice(product.getMrpPrice());
        entity.setSellingPrice(product.getSellingPrice());
        entity.setImages(product.getImages());
        entity.setHasIva(product.isHasIva());
        entity.setIvaPercentage(product.getIvaPercentage());
        entity.setNumRatings(product.getNumRatings());
        entity.setStock(product.getStock());
        entity.setCategory(categoryEntity);
        entity.setDiscountPercentage(product.getDiscountPercentage());
        return entity;
    }

    default Product toDomain(ProductEntity entity, CategoryEntityMapper categoryEntityMapper) {
        if (entity == null) {
            return null;
        }
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                entity.getMrpPrice(),
                entity.getSellingPrice(),
                entity.getImages(),
                entity.isHasIva(),
                entity.getIvaPercentage(),
                entity.getNumRatings(),
                entity.getStock(),
                categoryEntityMapper.toDomain(entity.getCategory())
        );
    }
}
