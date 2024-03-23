package com.heygongc.user.exception;

import com.heygongc.global.error.exception.UnauthenticatedException;

public class UserExpiredTokenException extends UnauthenticatedException {

    public UserExpiredTokenException() {
        super(UserErrorType.EXPIRED_TOKEN.name(), UserErrorType.EXPIRED_TOKEN.getMessage());
    }

    public UserExpiredTokenException(String message) {
        super(UserErrorType.EXPIRED_TOKEN.name(), message);
    }

}
