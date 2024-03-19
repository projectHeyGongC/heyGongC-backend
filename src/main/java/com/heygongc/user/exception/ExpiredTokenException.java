package com.heygongc.user.exception;

import com.heygongc.global.error.exception.UnauthenticatedException;

public class ExpiredTokenException extends UnauthenticatedException {

    public ExpiredTokenException() {
        super(UserErrorType.EXPIRED_TOKEN.name(), UserErrorType.EXPIRED_TOKEN.getMessage());
    }

    public ExpiredTokenException(String message) {
        super(UserErrorType.EXPIRED_TOKEN.name(), message);
    }
}
