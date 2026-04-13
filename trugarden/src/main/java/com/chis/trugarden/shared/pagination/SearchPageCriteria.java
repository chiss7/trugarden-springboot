package com.chis.trugarden.shared.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class SearchPageCriteria {

    @NotNull(message = "El campo page es requerido")
    private int page;

    @NotNull(message = "El campo size es requerido")
    private int size;

    private String sortBy;

    private Direction sortOrder;

    private List<SearchFilter> filters;

    protected SearchPageCriteria() {
        this.page = 0;
        this.size = 10;
        this.sortOrder = Direction.DESC;
        this.filters = new ArrayList<>();
    }

    protected void addFilter(SearchFilter filter) {
        this.filters.add(filter);
    }

    @JsonIgnore
    public Pageable getPageable() {
        return PageRequest.of(
                this.getPage(),
                this.getSize(),
                this.getSortOrder(),
                this.getSortBy()
        );
    }
}
