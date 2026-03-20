package com.chis.trugarden.api.product.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
        @NotNull(message = "name is required")
        @NotBlank(message = "name cannot be blank")
        String name,

        @NotNull(message = "slug is required")
        @NotBlank(message = "slug cannot be blank")
        String slug,

        @NotNull(message = "description is required")
        @NotBlank(message = "description cannot be blank")
        String description,

        @NotNull(message = "mrpPrice is required")
        BigDecimal mrpPrice,

        @NotNull(message = "sellingPrice is required")
        BigDecimal sellingPrice,

        @NotNull(message = "images are required")
        @NotEmpty(message = "images cannot be empty")
        List<String> imageUrls,

        @NotNull(message = "category is required")
        @NotBlank(message = "category cannot be blank")
        String category,

        @NotNull(message = "stock is required")
        Integer stock,

        @NotNull(message = "hasIva is required")
        Boolean hasIva,
        int ivaPercentage
) {
}
