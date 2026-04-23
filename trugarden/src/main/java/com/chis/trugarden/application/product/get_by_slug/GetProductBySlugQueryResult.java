package com.chis.trugarden.application.product.get_by_slug;

import com.chis.trugarden.application.product.get_all_paged.ProductResponse;
import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;

public class GetProductBySlugQueryResult extends Result<ProductResponse> {
    protected GetProductBySlugQueryResult(boolean isSuccess, Error error, ProductResponse value) {
        super(isSuccess, error, value);
    }

    public static GetProductBySlugQueryResult success(ProductResponse value) {
        return new GetProductBySlugQueryResult(true, Error.NONE, value);
    }

    public static GetProductBySlugQueryResult failure(Error error) {
        return new GetProductBySlugQueryResult(false, error, null);
    }
}
