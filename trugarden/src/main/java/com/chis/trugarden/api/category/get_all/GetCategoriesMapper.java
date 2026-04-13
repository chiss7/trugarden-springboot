package com.chis.trugarden.api.category.get_all;

import com.chis.trugarden.application.category.get_all.CategoryResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GetCategoriesMapper {
    GetCategoriesResponse toResponse(CategoryResponse categoryResponse);
    List<GetCategoriesResponse> toResponse(List<CategoryResponse> categoryResponses);
}
