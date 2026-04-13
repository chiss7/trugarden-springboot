package com.chis.trugarden.api.product;

import com.chis.trugarden.api.product.create.CreateProductMapper;
import com.chis.trugarden.api.product.create.CreateProductRequest;
import com.chis.trugarden.api.product.create.CreateProductResponse;
import com.chis.trugarden.api.product.get_all_paged.GetProductsMapper;
import com.chis.trugarden.api.product.get_all_paged.GetProductsRequest;
import com.chis.trugarden.application.product.get_all_paged.GetProductsQueryResult;
import com.chis.trugarden.shared.api.ControllerBase;
import com.chis.trugarden.shared.api.GenericResponse;
import com.chis.trugarden.shared.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product Controller")
public class ProductController extends ControllerBase {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final CreateProductMapper createProductMapper;
    private final GetProductsMapper getProductsMapper;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GenericResponse<?>> createProduct(
            @RequestPart("product") @Valid CreateProductRequest request,
            @RequestPart("images") List<MultipartFile> images
    ) {
        Result<Long> result = commandGateway.sendAndWait(createProductMapper.toCommand(request, images));
        return result.isSuccess() ?
                created(result.getValue().toString(), new CreateProductResponse(result.getValue())) :
                error(result.getError());
    }

    @PostMapping("/paged")
    public ResponseEntity<GenericResponse<?>> paged(@RequestBody @Validated GetProductsRequest request) {
        GetProductsQueryResult result = queryGateway.query(getProductsMapper.toQuery(request), ResponseTypes.instanceOf(GetProductsQueryResult.class)).join();
        return result.isSuccess() ?
                success(result.getValue()) :
                error(result.getError());
    }
}
