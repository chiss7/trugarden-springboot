package com.chis.trugarden.application.product.get_all_paged;

import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;

public class GetProductsQueryResult extends Result<GetProductsResult> {
    protected GetProductsQueryResult(boolean isSuccess, Error error, GetProductsResult value) {
        super(isSuccess, error, value);
    }

    public static GetProductsQueryResult success(GetProductsResult value) {
        return new GetProductsQueryResult(true, Error.NONE, value);
    }

    public static GetProductsQueryResult failure(Error error) {
        return new GetProductsQueryResult(false, error, null);
    }
}
