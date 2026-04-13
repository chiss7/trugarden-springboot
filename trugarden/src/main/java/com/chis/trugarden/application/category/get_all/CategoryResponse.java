package com.chis.trugarden.application.category.get_all;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private final Long id;
    private final String name;
    private List<CategoryResponse> children;

    public static CategoryResponse of(Long id, String name) {
        return new CategoryResponse(id, name, new ArrayList<>());
    }

    public void addChild(CategoryResponse child) {
        this.children.add(child);
    }
}
