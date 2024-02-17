package com.heygongc.device.exception;

import com.heygongc.global.error.exception.ApiBusinessException;

public class DeviceNotFoundException extends ApiBusinessException {

    private final String code;

    public DeviceNotFoundException() {
        super(DeviceErrorType.DEVICE_NOT_FOUND.getMessage());
        this.code = DeviceErrorType.DEVICE_NOT_FOUND.name();
    }

    public DeviceNotFoundException(String message) {
        super(message);
        this.code = DeviceErrorType.DEVICE_NOT_FOUND.name();
    }

    public String getCode() {return code;}

}
