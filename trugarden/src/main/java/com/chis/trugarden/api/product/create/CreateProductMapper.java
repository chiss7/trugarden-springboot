package com.chis.trugarden.api.product.create;

import com.chis.trugarden.application.product.create.CreateProductCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateProductMapper {
    CreateProductCommand toCommand(CreateProductRequest request);
}
