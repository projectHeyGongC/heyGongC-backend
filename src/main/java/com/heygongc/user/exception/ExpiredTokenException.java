package com.heygongc.user.exception;

import com.heygongc.global.error.exception.UnauthenticatedException;

public class ExpiredTokenException extends UnauthenticatedException {

    private final String code;

    public ExpiredTokenException() {
        super(UserErrorType.EXPIRED_TOKEN.getMessage());
        this.code = UserErrorType.EXPIRED_TOKEN.name();
    }

    public ExpiredTokenException(String message) {
        super(message);
        this.code = UserErrorType.EXPIRED_TOKEN.name();
    }

    public String getCode() {
        return code;
    }
}
