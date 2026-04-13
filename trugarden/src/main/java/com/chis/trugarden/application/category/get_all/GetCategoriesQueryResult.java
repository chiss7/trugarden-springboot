package com.chis.trugarden.application.category.get_all;

import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;

import java.util.List;

public class GetCategoriesQueryResult extends Result<List<CategoryResponse>> {
    protected GetCategoriesQueryResult(boolean isSuccess, Error error, List<CategoryResponse> values) {
        super(isSuccess, error, values);
    }

    public static GetCategoriesQueryResult success(List<CategoryResponse> values) {
        return new GetCategoriesQueryResult(true, Error.NONE, values);
    }

    public static GetCategoriesQueryResult failure(Error error) {
        return new GetCategoriesQueryResult(false, error, null);
    }
}
