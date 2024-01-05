package com.heygongc.user.exception;

import com.heygongc.global.error.exception.ApiBusinessException;

public class AlreadyLeftException extends ApiBusinessException {

    private final String code;

    public AlreadyLeftException() {
        super(UserErrorType.ALREADY_LEFT.getMessage());
        this.code = UserErrorType.ALREADY_LEFT.name();
    }

    public AlreadyLeftException(String message) {
        super(message);
        this.code = UserErrorType.ALREADY_LEFT.name();
    }

    public String getCode() {
        return code;
    }
}
