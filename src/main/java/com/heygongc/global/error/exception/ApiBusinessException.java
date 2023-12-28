package com.heygongc.global.error.exception;

import com.heygongc.global.error.ErrorType;

public class ApiBusinessException extends RuntimeException {

    private final String code;

    public ApiBusinessException() {
        super(ErrorType.UNAUTHENTICATED.getMessage());
        this.code = ErrorType.UNAUTHENTICATED.name();
    }
    public ApiBusinessException(String message) {
        super(message);
        this.code = ErrorType.UNAUTHENTICATED.name();
    }

    public String getCode() {
        return code;
    }
}
