package com.chis.trugarden.api.product.create;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import com.chis.trugarden.shared.enums.ProductType;
import jakarta.validation.constraints.Min;
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

        @NotNull(message = "originalPrice is required")
        BigDecimal originalPrice,

        @NotNull(message = "unitPrice is required")
        BigDecimal unitPrice,

        @NotNull(message = "category is required")
        @NotBlank(message = "category cannot be blank")
        String category,

        @NotNull(message = "stock is required")
        Integer stock,

        @NotNull(message = "leadTimeMinDays is required")
        @Min(value = 0, message = "leadTimeMinDays must be >= 0")
        Integer leadTimeMinDays,

        @NotNull(message = "leadTimeMaxDays is required")
        @Min(value = 0, message = "leadTimeMaxDays must be >= 0")
        Integer leadTimeMaxDays,

        ProductType productType,

        @NotNull(message = "hasIva is required")
        Boolean hasIva,
        Integer ivaPercentage
) {
        @AssertTrue(message = "ivaPercentage is required")
        public boolean isIvaValid() {
                if (Boolean.TRUE.equals(hasIva)) {
                        return ivaPercentage != null && ivaPercentage > 0;
                }
                return true;
        }

        @AssertTrue(message = "leadTimeMinDays must be <= leadTimeMaxDays")
        public boolean isLeadTimeValid() {
                if (leadTimeMinDays == null || leadTimeMaxDays == null) {
                        return true;
                }
                return leadTimeMinDays <= leadTimeMaxDays;
        }
}
