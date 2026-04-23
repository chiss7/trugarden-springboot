package com.chis.trugarden.application.product.get_by_slug;

import com.chis.trugarden.application.product.abstractions.ProductRepository;
import com.chis.trugarden.application.product.get_all_paged.ProductResponseMapper;
import com.chis.trugarden.domain.product.Product;
import com.chis.trugarden.domain.product.ProductErrors;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetProductBySlugQueryHandler {
    private final ProductRepository productRepository;
    private final ProductResponseMapper productResponseMapper;

    @QueryHandler
    public GetProductBySlugQueryResult handle(GetProductBySlugQuery query) {
        Optional<Product> productOpt = productRepository.findBySlug(query.slug());
        if (productOpt.isEmpty()) {
            return GetProductBySlugQueryResult.failure(ProductErrors.notFound(query.slug()));
        }

        Product product = productOpt.get();
        return GetProductBySlugQueryResult.success(productResponseMapper.toResponse(product));
    }
}
