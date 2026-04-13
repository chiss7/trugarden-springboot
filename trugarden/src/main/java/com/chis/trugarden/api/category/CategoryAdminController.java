package com.chis.trugarden.api.category;

import com.chis.trugarden.api.category.create.CreateCategoryMapper;
import com.chis.trugarden.api.category.create.CreateCategoryRequest;
import com.chis.trugarden.api.category.create.CreateCategoryResponse;
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
@RequestMapping("/admin/category")
@RequiredArgsConstructor
@Tag(name = "Category Admin Controller")
public class CategoryAdminController extends ControllerBase {
    private final CommandGateway commandGateway;
    private final CreateCategoryMapper createCategoryMapper;

    @PostMapping
    public ResponseEntity<GenericResponse<?>> createCategory(@RequestBody @Valid CreateCategoryRequest request) {
        Result<Long> result = commandGateway.sendAndWait(createCategoryMapper.toCommand(request));
        return result.isSuccess() ?
                created(result.getValue().toString(), new CreateCategoryResponse(result.getValue())) :
                error(result.getError());
    }
}
