package com.chis.trugarden.api.product.create;

import com.chis.trugarden.application.product.create.CreateProductCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CreateProductMapper {
    @Mapping(target = "images", source = "images")
    CreateProductCommand toCommand(CreateProductRequest request, List<MultipartFile> images);
}
