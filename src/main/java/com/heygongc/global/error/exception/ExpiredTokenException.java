package com.heygongc.global.error.exception;

import com.heygongc.global.error.ErrorType;

import java.io.Serial;

public class ExpiredTokenException extends UnauthenticatedException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    public ExpiredTokenException() {
        super(ErrorType.EXPIRED_TOKEN.name());
        this.code = ErrorType.EXPIRED_TOKEN.getMessage();
    }

    public ExpiredTokenException(String message) {
        super(message);
        this.code = ErrorType.EXPIRED_TOKEN.name();
    }

    public String getCode() {
        return code;
    }
}
