package com.chis.trugarden.api.category.create;

import com.chis.trugarden.application.category.create.CreateCategoryCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateCategoryMapper {
    CreateCategoryCommand toCommand(CreateCategoryRequest request);
}
