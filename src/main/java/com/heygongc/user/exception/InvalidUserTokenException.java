package com.heygongc.user.exception;

import com.heygongc.global.error.exception.UnauthenticatedException;

public class InvalidUserTokenException extends UnauthenticatedException {

    private final String code;

    public InvalidUserTokenException() {
        super(UserErrorType.INVALID_TOKEN.getMessage());
        this.code = UserErrorType.INVALID_TOKEN.name();
    }

    public InvalidUserTokenException(String message) {
        super(message);
        this.code = UserErrorType.INVALID_TOKEN.name();
    }

    public String getCode() {
        return code;
    }
}
