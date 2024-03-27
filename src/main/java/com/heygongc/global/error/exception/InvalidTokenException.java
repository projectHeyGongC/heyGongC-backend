package com.heygongc.global.error.exception;

import com.heygongc.global.error.ErrorType;

import java.io.Serial;

public class InvalidTokenException extends UnauthenticatedException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    public InvalidTokenException() {
        super(ErrorType.INVALID_TOKEN.getMessage());
        this.code = ErrorType.INVALID_TOKEN.name();
    }

    public InvalidTokenException(String message) {
        super(message);
        this.code = ErrorType.INVALID_TOKEN.name();
    }

    public String getCode() {
        return code;
    }
}
