package com.chis.trugarden.application.category.get_all;

import com.chis.trugarden.domain.category.Category;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CategoryTreeMapper {

    public static List<CategoryResponse> toTree(List<Category> leafCategories) {
        // id -> created node
        Map<Long, CategoryResponse> nodeMap = new LinkedHashMap<>();

        for (Category leaf : leafCategories) {
            processChain(leaf, nodeMap);
        }

        // Only return roots (level 1)
        return nodeMap.values().stream()
                .filter(node -> isRoot(node, leafCategories))
                .toList();
    }

    private static void processChain(Category category, Map<Long, CategoryResponse> nodeMap) {
        if (category == null) return;

        // process parent recursively
        processChain(category.getParentCategory(), nodeMap);

        // create node if not exists
        nodeMap.computeIfAbsent(category.getId(), id ->
                CategoryResponse.of(category.getId(), category.getName())
        );

        // link this node to parent
        Category parent = category.getParentCategory();
        if (parent != null) {
            CategoryResponse parentNode = nodeMap.get(parent.getId());
            CategoryResponse thisNode = nodeMap.get(category.getId());

            boolean alreadyLinked = parentNode.getChildren().stream()
                    .anyMatch(c -> c.getId().equals(category.getId()));

            if (!alreadyLinked) {
                parentNode.getChildren().add(thisNode);
            }
        }
    }

    private static boolean isRoot(CategoryResponse node, List<Category> leafCategories) {
        // A node is root if no other category in the chain has it as a child
        return leafCategories.stream()
                .flatMap(CategoryTreeMapper::ancestorStream)
                .filter(c -> c.getParentCategory() != null)
                .noneMatch(c -> c.getId().equals(node.getId()) &&
                        c.getParentCategory() != null);
    }

    private static Stream<Category> ancestorStream(Category category) {
        List<Category> chain = new ArrayList<>();
        Category current = category;
        while (current != null) {
            chain.add(current);
            current = current.getParentCategory();
        }
        return chain.stream();
    }
}
