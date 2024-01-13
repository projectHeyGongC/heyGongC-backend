package com.heygongc.user.exception;

import com.heygongc.global.error.exception.ApiBusinessException;

public class UserNotFoundException extends ApiBusinessException {

    private final String code;

    public UserNotFoundException() {
        super(UserErrorType.USER_NOT_FOUND.getMessage());
        this.code = UserErrorType.USER_NOT_FOUND.name();
    }

    public UserNotFoundException(String message) {
        super(message);
        this.code = UserErrorType.USER_NOT_FOUND.name();
    }

    public String getCode() {
        return code;
    }
}
