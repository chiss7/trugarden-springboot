package com.chis.trugarden.api.category;

import com.chis.trugarden.api.category.get_all.GetCategoriesMapper;
import com.chis.trugarden.application.category.get_all.GetCategoriesQuery;
import com.chis.trugarden.application.category.get_all.GetCategoriesQueryResult;
import com.chis.trugarden.shared.api.ControllerBase;
import com.chis.trugarden.shared.api.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name = "Category Controller")
public class CategoryController extends ControllerBase {
    private final QueryGateway queryGateway;
    private final GetCategoriesMapper getCategoriesMapper;

    @GetMapping
    public ResponseEntity<GenericResponse<?>> getAll() {
        GetCategoriesQueryResult result = queryGateway.query(new GetCategoriesQuery(), ResponseTypes.instanceOf(GetCategoriesQueryResult.class)).join();
        return result.isSuccess() ?
                success(getCategoriesMapper.toResponse(result.getValue())) :
                error(result.getError());
    }
}