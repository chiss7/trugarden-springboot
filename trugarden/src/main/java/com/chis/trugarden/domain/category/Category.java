package com.chis.trugarden.domain.category;

public class Category {
    private final Long id;
    private final String name;
    private final String categoryCode;
    private final Category parentCategory;
    private final Integer level;

    public Category(
            Long id,
            String name,
            String categoryCode,
            Category parentCategory,
            Integer level
    ) {
        this.id = id;
        this.name = name;
        this.categoryCode = categoryCode;
        this.parentCategory = parentCategory;
        this.level = level;
    }

    public static Category of(
            Long id,
            String name,
            String categoryCode,
            Category parentCategory,
            Integer level
    ) {
        return new Category(id, name, categoryCode, parentCategory, level);
    }

    public static Category of(
            String name,
            String categoryCode,
            Category parentCategory,
            Integer level
    ) {
        return new Category(null, name, categoryCode, parentCategory, level);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public Integer getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                ", parentCategory=" + parentCategory +
                ", level=" + level +
                '}';
    }
}
