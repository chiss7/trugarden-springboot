package com.chis.trugarden.application.product.get_all_paged;

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
        List<String> images,
        String category
) {
}
