package com.chis.trugarden.application.cart.get;

import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;

public class GetUserCartQueryResult extends Result<GetUserCartResult> {
    protected GetUserCartQueryResult(boolean isSuccess, Error error, GetUserCartResult value) {
        super(isSuccess, error, value);
    }

    public static GetUserCartQueryResult success(GetUserCartResult value) {
        return new GetUserCartQueryResult(true, Error.NONE, value);
    }

    public static GetUserCartQueryResult failure(Error error) {
        return new GetUserCartQueryResult(false, error, null);
    }
}
