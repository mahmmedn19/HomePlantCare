package com.project.homeplantcare.data.utils;

public class Result<T> {
    private final T data;
    private final String errorMessage;
    private final Status status;

    public enum Status {
        SUCCESS, ERROR, LOADING
    }

    private Result(T data, String errorMessage, Status status) {
        this.data = data;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, Status.SUCCESS);
    }

    public static <T> Result<T> error(String errorMessage) {
        return new Result<>(null, errorMessage, Status.ERROR);
    }

    public static <T> Result<T> loading() {
        return new Result<>(null, null, Status.LOADING);
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Status getStatus() {
        return status;
    }
}
