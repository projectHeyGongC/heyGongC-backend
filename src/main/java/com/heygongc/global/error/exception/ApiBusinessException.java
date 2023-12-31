package com.heygongc.global.error.exception;

import com.heygongc.global.error.ErrorType;

public class ApiBusinessException extends RuntimeException {

    private final String code;

    public ApiBusinessException() {
        super(ErrorType.BAD_REQUEST.getMessage());
        this.code = ErrorType.BAD_REQUEST.name();
    }
    public ApiBusinessException(String message) {
        super(message);
        this.code = ErrorType.BAD_REQUEST.name();
    }

    public ApiBusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
