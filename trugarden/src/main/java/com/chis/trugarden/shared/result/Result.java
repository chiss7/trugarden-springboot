package com.chis.trugarden.shared.result;

public class Result<T> {

    private final boolean isSuccess;
    private final Error error;
    private final T value;

    protected Result(boolean isSuccess, Error error, T value) {
        if (isSuccess && error != Error.NONE || !isSuccess && error == Error.NONE) {
            throw new IllegalArgumentException("Invalid error state");
        }
        this.isSuccess = isSuccess;
        this.error = error;
        this.value = value;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public Error getError() {
        return error;
    }

    public T getValue() {
        if (!isSuccess) {
            throw new IllegalStateException("Cannot access value on a failure result");
        }
        return value;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(true, Error.NONE, value);
    }

    public static Result<Void> success() {
        return new Result<>(true, Error.NONE, null);
    }

    public static <T> Result<T> failure(Error error) {
        return new Result<>(false, error, null);
    }
}
