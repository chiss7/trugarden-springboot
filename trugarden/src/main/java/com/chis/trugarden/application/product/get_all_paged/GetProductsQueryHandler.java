package com.chis.trugarden.application.product.get_all_paged;

import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.shared.pagination.SearchFilter;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProductsQueryHandler {
    private final ProductRepository  productRepository;
    private final ProductResponseMapper productResponseMapper;

    @QueryHandler
    public GetProductsQueryResult handle(GetProductsQuery query) {
        List<SearchFilter> filters = new ArrayList<>(query.getFilters());
        Page<Product> productPage = productRepository.findAll(filters, query.getPageable());
        Page<ProductResponse> productResponsePage = productPage.map(productResponseMapper::toResponse);
        GetProductsResult getProductsResult = new GetProductsResult(productResponsePage);
        getProductsResult.setContent(productResponsePage.getContent());
        return GetProductsQueryResult.success(getProductsResult);
    }
}
