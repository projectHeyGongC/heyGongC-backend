package com.heygongc.notification.exception;

import com.heygongc.global.error.exception.ApiBusinessException;

public class NotificationNotFoundException extends ApiBusinessException {

    private final String code;

    public NotificationNotFoundException() {
        super(NotificationErrorType.NOTIFICATION_NOT_FOUND.getMessage());
        this.code = NotificationErrorType.NOTIFICATION_NOT_FOUND.name();
    }

    public NotificationNotFoundException(String message) {
        super(message);
        this.code = NotificationErrorType.NOTIFICATION_NOT_FOUND.name();
    }

    public String getCode() {return code;}

}
