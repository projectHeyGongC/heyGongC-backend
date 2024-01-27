package com.heygongc.user.exception;

import com.heygongc.global.error.exception.ApiBusinessException;

public class AlreadySignInException extends ApiBusinessException {

    private final String code;

    public AlreadySignInException() {
        super(UserErrorType.ALREADY_SIGN_IN.getMessage());
        this.code = UserErrorType.ALREADY_SIGN_IN.name();
    }

    public AlreadySignInException(String message) {
        super(message);
        this.code = UserErrorType.ALREADY_SIGN_IN.name();
    }

    public String getCode() {
        return code;
    }
}
