package com.chis.trugarden.application.category.get_all;

import com.chis.trugarden.application.category.abstractions.CategoryRepository;
import com.chis.trugarden.domain.category.Category;
import com.chis.trugarden.shared.util.Constants;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCategoriesQueryHandler {
    private final CategoryRepository categoryRepository;

    @QueryHandler
    public GetCategoriesQueryResult handle(GetCategoriesQuery getCategoriesQuery) {
        List<Category> categories = categoryRepository.findByLevel(Constants.THIRD_LEVEL);
        List<CategoryResponse> categoryResponses = CategoryTreeMapper.toTree(categories);
        return GetCategoriesQueryResult.success(categoryResponses);
    }
}
