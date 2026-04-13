package com.chis.trugarden.shared.pagination;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchPageResponse<T> {
    private List<T> content;
    private int currentPage;
    private int totalElements;
    private int totalPages;
    private boolean isEmpty;
    private int size;
    private long numberOfElements;

    public SearchPageResponse(Page<?> page) {
        this.currentPage = page.getNumber();
        this.totalElements = (int) page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.isEmpty = page.isEmpty();
        this.size = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
    }
}
