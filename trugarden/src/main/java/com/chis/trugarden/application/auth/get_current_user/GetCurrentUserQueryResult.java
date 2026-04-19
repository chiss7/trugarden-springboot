package com.chis.trugarden.application.auth.get_current_user;

import com.chis.trugarden.shared.result.Error;
import com.chis.trugarden.shared.result.Result;

public class GetCurrentUserQueryResult extends Result<GetCurrentUserResult> {
    protected GetCurrentUserQueryResult(boolean isSuccess, Error error, GetCurrentUserResult value) {
        super(isSuccess, error, value);
    }

    public static GetCurrentUserQueryResult success(GetCurrentUserResult value) {
        return new GetCurrentUserQueryResult(true, Error.NONE, value);
    }

    public static GetCurrentUserQueryResult failure(Error error) {
        return new GetCurrentUserQueryResult(false, error, null);
    }
}
