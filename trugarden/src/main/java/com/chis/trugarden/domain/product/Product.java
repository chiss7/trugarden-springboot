package com.chis.trugarden.domain.product;

import com.chis.trugarden.domain.category.Category;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class Product {

    private final Long id;
    private final String name;
    private final String slug;
    private final String description;
    private final BigDecimal mrpPrice;
    private final BigDecimal sellingPrice;
    private final List<String> images;
    private final double discountPercentage;
    private final boolean hasIva;
    private final int ivaPercentage;
    private final int numRatings;
    private final double stock;
    private final Category category;
    // private final List<Review> reviews;

    public Product(
            Long id,
            String name,
            String slug,
            String description,
            BigDecimal mrpPrice,
            BigDecimal sellingPrice,
            List<String> images,
            boolean hasIva,
            int ivaPercentage,
            int numRatings,
            double stock,
            Category category
    ) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "El nombre del producto no puede ser nulo");
        this.slug = Objects.requireNonNull(slug, "El slug del producto no puede ser nulo");
        this.description = description;
        this.mrpPrice = Objects.requireNonNull(mrpPrice, "El precio MRP del producto no puede ser nulo");
        this.sellingPrice = Objects.requireNonNull(sellingPrice, "El precio de venta del producto no puede ser nulo");
        this.images = Objects.requireNonNull(images, "La lista de imágenes del producto no puede ser nula");
        this.discountPercentage = calculatePercentageDiscount();
        this.hasIva = hasIva;
        this.ivaPercentage = ivaPercentage;
        this.numRatings = numRatings;
        this.stock = stock;
        this.category = Objects.requireNonNull(category, "La categoría del producto no puede ser nulo");
    }

    public static Product of(
            Long id,
            String name,
            String slug,
            String description,
            BigDecimal mrpPrice,
            BigDecimal sellingPrice,
            List<String> images,
            boolean hasIva,
            int ivaPercentage,
            int numRatings,
            double stock,
            Category category
    ) {
        return new Product(
                id,
                name,
                slug,
                description,
                mrpPrice,
                sellingPrice,
                images,
                hasIva,
                ivaPercentage,
                numRatings,
                stock,
                category
        );
    }

    public static Product of(
            String name,
            String slug,
            String description,
            BigDecimal mrpPrice,
            BigDecimal sellingPrice,
            List<String> images,
            boolean hasIva,
            int ivaPercentage,
            int numRatings,
            double stock,
            Category category
    ) {
        return new Product(
                null,
                name,
                slug,
                description,
                mrpPrice,
                sellingPrice,
                images,
                hasIva,
                ivaPercentage,
                numRatings,
                stock,
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

    public BigDecimal getMrpPrice() {
        return mrpPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
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

    public Category getCategory() {
        return category;
    }

    public double calculatePercentageDiscount() {
        if (mrpPrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        BigDecimal discount = mrpPrice.subtract(sellingPrice);
        return discount.divide(mrpPrice, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                ", mrpPrice=" + mrpPrice +
                ", sellingPrice=" + sellingPrice +
                ", images=" + images +
                ", discountPercentage=" + discountPercentage +
                ", hasIva=" + hasIva +
                ", ivaPercentage=" + ivaPercentage +
                ", numRatings=" + numRatings +
                ", stock=" + stock +
                ", categoryName='" + category.getName() + '\'' +
                '}';
    }
}
