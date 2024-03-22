package com.heygongc.global.error.exception;

import com.heygongc.global.error.ErrorResponse;
import com.heygongc.global.error.ErrorType;

import java.io.Serial;

public class UnauthenticatedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2L;

    private final String code;

    public UnauthenticatedException() {
        super(ErrorType.UNAUTHENTICATED.getMessage());
        this.code = ErrorType.UNAUTHENTICATED.name();
    }
    public UnauthenticatedException(String message) {
        super(message);
        this.code = ErrorType.UNAUTHENTICATED.name();
    }

    public UnauthenticatedException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
