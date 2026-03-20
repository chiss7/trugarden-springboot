package com.chis.trugarden.api.category.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
        @NotNull(message = "category is required")
        @NotBlank(message = "category cannot be blank")
        String category,
        String name,
        String category2,
        String name2,
        String category3,
        String name3
) {
}
