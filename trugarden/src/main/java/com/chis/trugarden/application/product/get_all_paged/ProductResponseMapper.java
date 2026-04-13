package com.chis.trugarden.application.product.get_all_paged;

import com.chis.trugarden.domain.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {
    @Mapping(target = "category", source = "category.name")
    ProductResponse toResponse(Product product);
}
