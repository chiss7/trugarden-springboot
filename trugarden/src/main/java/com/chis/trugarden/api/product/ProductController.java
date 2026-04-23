package com.chis.trugarden.api.product;

import com.chis.trugarden.api.product.get_all_paged.GetProductsMapper;
import com.chis.trugarden.api.product.get_all_paged.GetProductsRequest;
import com.chis.trugarden.application.product.get_all_paged.GetProductsQueryResult;
import com.chis.trugarden.application.product.get_by_slug.GetProductBySlugQuery;
import com.chis.trugarden.application.product.get_by_slug.GetProductBySlugQueryResult;
import com.chis.trugarden.shared.api.ControllerBase;
import com.chis.trugarden.shared.api.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product Controller")
public class ProductController extends ControllerBase {
    private final QueryGateway queryGateway;
    private final GetProductsMapper getProductsMapper;

    @PostMapping("/paged")
    public ResponseEntity<GenericResponse<?>> paged(@RequestBody @Validated GetProductsRequest request) {
        GetProductsQueryResult result = queryGateway.query(getProductsMapper.toQuery(request), ResponseTypes.instanceOf(GetProductsQueryResult.class)).join();
        return result.isSuccess() ?
                success(result.getValue()) :
                error(result.getError());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<GenericResponse<?>> getProductBySlug(@PathVariable String slug) {
        GetProductBySlugQueryResult result = queryGateway.query(new GetProductBySlugQuery(slug), ResponseTypes.instanceOf(GetProductBySlugQueryResult.class)).join();
        return result.isSuccess() ?
                success(result.getValue()) :
                error(result.getError());
    }
}
