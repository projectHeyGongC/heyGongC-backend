package com.heygongc.global.error.exception;

import com.heygongc.global.error.ErrorType;

import java.io.Serial;

public class ForbiddenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    public ForbiddenException() {
        super(ErrorType.FORBIDDEN.getMessage());
        this.code = ErrorType.FORBIDDEN.name();
    }
    public ForbiddenException(String message) {
        super(message);
        this.code = ErrorType.FORBIDDEN.name();
    }

    public String getCode() {
        return code;
    }
}
