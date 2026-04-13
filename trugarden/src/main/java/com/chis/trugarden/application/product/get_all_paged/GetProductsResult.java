package com.chis.trugarden.application.product.get_all_paged;

import com.chis.trugarden.shared.pagination.SearchPageResponse;
import org.springframework.data.domain.Page;

public class GetProductsResult extends SearchPageResponse<ProductResponse> {
    public GetProductsResult(Page<?> page) {
        super(page);
    }
}
