package com.heygongc.device.exception;

import com.heygongc.global.error.exception.UnauthenticatedException;

public class InvalidDeviceTokenException extends UnauthenticatedException {

    private final String code;

    public InvalidDeviceTokenException() {
        super(DeviceErrorType.INVALID_TOKEN.getMessage());
        this.code = DeviceErrorType.INVALID_TOKEN.name();
    }

    public InvalidDeviceTokenException(String message) {
        super(message);
        this.code = DeviceErrorType.INVALID_TOKEN.name();
    }

    public String getCode() {
        return code;
    }
}
