package com.heygongc.user.exception;

import com.heygongc.global.error.exception.ApiBusinessException;

public class InvalidTokenException extends ApiBusinessException {

    private final String code;

    public InvalidTokenException() {
        super(UserErrorType.INVALID_TOKEN.getMessage());
        this.code = UserErrorType.INVALID_TOKEN.name();
    }

    public InvalidTokenException(String message) {
        super(message);
        this.code = UserErrorType.INVALID_TOKEN.name();
    }

    public String getCode() {
        return code;
    }
}
