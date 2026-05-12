package com.chis.trugarden.domain.product;

import com.chis.trugarden.domain.category.Category;
import com.chis.trugarden.shared.enums.ProductType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class Product {

    private final Long id;
    private final String name;
    private final String slug;
    private final String description;
    private final BigDecimal originalPrice;
    private final BigDecimal unitPrice;
    private final List<String> images;
    private final double discountPercentage;
    private final boolean hasIva;
    private final int ivaPercentage;
    private final int numRatings;
    private final double stock;
    private final int leadTimeMinDays;
    private final int leadTimeMaxDays;
    private final ProductType productType;
    private final Category category;
    // private final List<Review> reviews;

    public Product(
            Long id,
            String name,
            String slug,
            String description,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            List<String> images,
            boolean hasIva,
            int ivaPercentage,
            int numRatings,
            double stock,
            int leadTimeMinDays,
            int leadTimeMaxDays,
            ProductType productType,
            Category category
    ) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "El nombre del producto no puede ser nulo");
        this.slug = Objects.requireNonNull(slug, "El slug del producto no puede ser nulo");
        this.description = description;
        this.originalPrice = Objects.requireNonNull(originalPrice, "El precio original del producto no puede ser nulo");
        this.unitPrice = Objects.requireNonNull(unitPrice, "El precio unitario del producto no puede ser nulo");
        this.images = Objects.requireNonNull(images, "La lista de imágenes del producto no puede ser nula");
        this.discountPercentage = calculatePercentageDiscount();
        this.hasIva = hasIva;
        this.ivaPercentage = ivaPercentage;
        this.numRatings = numRatings;
        this.stock = stock;
        this.leadTimeMinDays = leadTimeMinDays;
        this.leadTimeMaxDays = leadTimeMaxDays;
        this.productType = productType != null ? productType : ProductType.STOCK;
        this.category = Objects.requireNonNull(category, "La categoría del producto no puede ser nulo");
    }

    public static Product of(
            Long id,
            String name,
            String slug,
            String description,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            List<String> images,
            boolean hasIva,
            int ivaPercentage,
            int numRatings,
            double stock,
            int leadTimeMinDays,
            int leadTimeMaxDays,
            ProductType productType,
            Category category
    ) {
        return new Product(
                id,
                name,
                slug,
                description,
                originalPrice,
                unitPrice,
                images,
                hasIva,
                ivaPercentage,
                numRatings,
                stock,
                leadTimeMinDays,
                leadTimeMaxDays,
                productType,
                category
        );
    }

    public static Product of(
            String name,
            String slug,
            String description,
            BigDecimal originalPrice,
            BigDecimal unitPrice,
            List<String> images,
            boolean hasIva,
            int ivaPercentage,
            int numRatings,
            double stock,
            int leadTimeMinDays,
            int leadTimeMaxDays,
            ProductType productType,
            Category category
    ) {
        return new Product(
                null,
                name,
                slug,
                description,
                originalPrice,
                unitPrice,
                images,
                hasIva,
                ivaPercentage,
                numRatings,
                stock,
                leadTimeMinDays,
                leadTimeMaxDays,
                productType,
                category
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public List<String> getImages() {
        return images;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public boolean isHasIva() {
        return hasIva;
    }

    public int getIvaPercentage() {
        return ivaPercentage;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public double getStock() {
        return stock;
    }

    public int getLeadTimeMinDays() {
        return leadTimeMinDays;
    }

    public int getLeadTimeMaxDays() {
        return leadTimeMaxDays;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Category getCategory() {
        return category;
    }

    /**
     * Calculates the discount percentage between original price and unit price.
     */
    public double calculatePercentageDiscount() {
        if (originalPrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        BigDecimal discount = originalPrice.subtract(unitPrice);
        return discount.divide(originalPrice, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    /**
     * Returns the unit price with IVA included.
     * If hasIva is false, returns the base unitPrice.
     */
    public BigDecimal getUnitPriceWithTax() {
        if (!hasIva || ivaPercentage == 0) {
            return unitPrice;
        }
        BigDecimal taxMultiplier = BigDecimal.ONE.add(
                BigDecimal.valueOf(ivaPercentage).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
        );
        return unitPrice.multiply(taxMultiplier).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns the original price with IVA included.
     * If hasIva is false, returns the base originalPrice.
     */
    public BigDecimal getOriginalPriceWithTax() {
        if (!hasIva || ivaPercentage == 0) {
            return originalPrice;
        }
        BigDecimal taxMultiplier = BigDecimal.ONE.add(
                BigDecimal.valueOf(ivaPercentage).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
        );
        return originalPrice.multiply(taxMultiplier).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns the tax amount for the unit price.
     */
    public BigDecimal getUnitPriceTaxAmount() {
        if (!hasIva || ivaPercentage == 0) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(
                BigDecimal.valueOf(ivaPercentage).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
        ).setScale(2, RoundingMode.HALF_UP);
    }

    public Product withStock(double newStock) {
        return new Product(
                this.id,
                this.name,
                this.slug,
                this.description,
                this.originalPrice,
                this.unitPrice,
                this.images,
                this.hasIva,
                this.ivaPercentage,
                this.numRatings,
                newStock,
                this.leadTimeMinDays,
                this.leadTimeMaxDays,
                this.productType,
                this.category
        );
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                ", originalPrice=" + originalPrice +
                ", unitPrice=" + unitPrice +
                ", images=" + images +
                ", discountPercentage=" + discountPercentage +
                ", hasIva=" + hasIva +
                ", ivaPercentage=" + ivaPercentage +
                ", numRatings=" + numRatings +
                ", stock=" + stock +
                ", leadTimeMinDays=" + leadTimeMinDays +
                ", leadTimeMaxDays=" + leadTimeMaxDays +
                ", productType=" + productType +
                ", categoryName='" + category.getName() + '\'' +
                '}';
    }
}
