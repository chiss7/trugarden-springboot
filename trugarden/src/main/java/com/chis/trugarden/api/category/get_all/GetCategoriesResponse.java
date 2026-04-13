package com.chis.trugarden.api.category.get_all;

import java.util.List;

public record GetCategoriesResponse(
        Long id,
        String name,
        List<GetCategoriesResponse> children
) {
}
