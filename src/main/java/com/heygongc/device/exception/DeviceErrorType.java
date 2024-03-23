package com.heygongc.device.exception;

public enum DeviceErrorType {
    DEVICE_NOT_FOUND("기기가 존재하지 않습니다."),

    INVALID_TOKEN ("유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN ("만료된 토큰입니다.");

    private final String message;
    DeviceErrorType(String message) {
        this.message = message;
    }

    public String getMessage() { return message;}
}
