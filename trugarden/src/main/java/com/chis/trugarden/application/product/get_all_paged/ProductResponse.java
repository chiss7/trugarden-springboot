package com.chis.trugarden.application.product.get_all_paged;

import com.chis.trugarden.shared.enums.ProductType;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String slug,
        String name,
        String description,
        BigDecimal originalPrice,
        BigDecimal unitPrice,
        double discountPercentage,
        boolean hasIva,
        int ivaPercentage,
        int numRatings,
        double stock,
        int leadTimeMinDays,
        int leadTimeMaxDays,
        ProductType productType,
        List<String> images,
        String category
) {
}
