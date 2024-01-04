package com.heygongc.user.exception;

import com.heygongc.global.error.exception.ApiBusinessException;

public class NewLoginDetectedException extends ApiBusinessException {

    private final String code;

    public NewLoginDetectedException() {
        super(UserErrorType.NEW_LOGIN_DETECTED.getMessage());
        this.code = UserErrorType.NEW_LOGIN_DETECTED.name();
    }

    public NewLoginDetectedException(String message) {
        super(message);
        this.code = UserErrorType.NEW_LOGIN_DETECTED.name();
    }

    public String getCode() {
        return code;
    }
}
