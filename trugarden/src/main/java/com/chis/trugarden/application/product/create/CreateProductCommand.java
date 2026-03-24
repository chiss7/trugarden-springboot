package com.chis.trugarden.application.product.create;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductCommand(
        String name,
        String slug,
        String description,
        BigDecimal originalPrice,
        BigDecimal unitPrice,
        List<String> imageUrls,
        String category,
        int stock,
        boolean hasIva,
        int ivaPercentage
) {
}
