package com.heygongc.device.exception;

import com.heygongc.global.error.exception.UnauthenticatedException;

public class DeviceExpiredTokenException extends UnauthenticatedException {

    public DeviceExpiredTokenException() {
        super(DeviceErrorType.EXPIRED_TOKEN.name(), DeviceErrorType.EXPIRED_TOKEN.getMessage());
    }

    public DeviceExpiredTokenException(String message) {
        super(DeviceErrorType.EXPIRED_TOKEN.name(), message);
    }

}
