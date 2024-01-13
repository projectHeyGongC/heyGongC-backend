package com.heygongc.user.exception;

import com.heygongc.global.error.exception.ApiBusinessException;

public class AlreadySignUpException extends ApiBusinessException {

    private final String code;

    public AlreadySignUpException() {
        super(UserErrorType.ALREADY_SIGN_UP.getMessage());
        this.code = UserErrorType.ALREADY_SIGN_UP.name();
    }

    public AlreadySignUpException(String message) {
        super(message);
        this.code = UserErrorType.ALREADY_SIGN_UP.name();
    }

    public String getCode() {
        return code;
    }
}
