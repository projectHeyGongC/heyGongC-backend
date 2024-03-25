package com.heygongc.global.error.exception;

import com.heygongc.global.error.ErrorType;

public class ExpiredTokenException extends UnauthenticatedException {

    public ExpiredTokenException() {
        super(ErrorType.EXPIRED_TOKEN.name(), ErrorType.EXPIRED_TOKEN.getMessage());
    }

    public ExpiredTokenException(String message) {
        super(ErrorType.EXPIRED_TOKEN.name(), message);
    }

}
