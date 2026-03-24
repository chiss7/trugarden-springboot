package com.chis.trugarden.persistence.product;

import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.persistence.category.CategoryEntityMapper;
import com.chis.trugarden.persistence.product.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    CategoryEntityMapper categoryEntityMapper = Mappers.getMapper(CategoryEntityMapper.class);

    default ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setSlug(product.getSlug());
        entity.setDescription(product.getDescription());
        entity.setOriginalPrice(product.getOriginalPrice());
        entity.setUnitPrice(product.getUnitPrice());
        entity.setImages(product.getImages());
        entity.setHasIva(product.isHasIva());
        entity.setIvaPercentage(product.getIvaPercentage());
        entity.setNumRatings(product.getNumRatings());
        entity.setStock(product.getStock());
        entity.setCategory(categoryEntityMapper.toEntity(product.getCategory()));
        entity.setDiscountPercentage(product.getDiscountPercentage());
        return entity;
    }

    default Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                entity.getOriginalPrice(),
                entity.getUnitPrice(),
                entity.getImages(),
                entity.isHasIva(),
                entity.getIvaPercentage(),
                entity.getNumRatings(),
                entity.getStock(),
                categoryEntityMapper.toDomain(entity.getCategory())
        );
    }
}
