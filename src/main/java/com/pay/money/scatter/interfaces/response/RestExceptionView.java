package com.pay.money.scatter.interfaces.response;

import org.springframework.http.HttpStatus;

public class RestExceptionView {
    private final int status;

    private final String exception;

    private final String message;

    public static RestExceptionView of(final HttpStatus status, final String exception, final String message) {
        return new RestExceptionView(status.value(), exception, message);
    }

    public RestExceptionView(final int status, final String exception, final String message) {
        this.status = status;
        this.exception = exception;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }
}
