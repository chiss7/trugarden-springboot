package com.chis.trugarden.api.product;

import com.chis.trugarden.api.product.create.CreateProductMapper;
import com.chis.trugarden.api.product.create.CreateProductRequest;
import com.chis.trugarden.api.product.create.CreateProductResponse;
import com.chis.trugarden.shared.api.ControllerBase;
import com.chis.trugarden.shared.api.GenericResponse;
import com.chis.trugarden.shared.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product Controller")
public class ProductController extends ControllerBase {
    private final CommandGateway commandGateway;
    private final CreateProductMapper createProductMapper;

    @PostMapping
    public ResponseEntity<GenericResponse<?>> createProduct(@RequestBody @Valid CreateProductRequest request) {
        Result<Long> result = commandGateway.sendAndWait(createProductMapper.toCommand(request));
        return result.isSuccess() ?
                created(result.getValue().toString(), new CreateProductResponse(result.getValue())) :
                error(result.getError());
    }
}
