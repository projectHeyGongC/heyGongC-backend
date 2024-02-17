package com.heygongc.device.exception;

public enum DeviceErrorType {
    DEVICE_NOT_FOUND("기기가 존재하지 않습니다.");

    private final String message;
    DeviceErrorType(String message) {
        this.message = message;
    }

    public String getMessage() { return message;}
}
