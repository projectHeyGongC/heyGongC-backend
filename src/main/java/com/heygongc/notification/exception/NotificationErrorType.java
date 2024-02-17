package com.heygongc.notification.exception;

public enum NotificationErrorType {

    NOTIFICATION_NOT_FOUND("알림이 존재하지 않습니다.");

    private final String message;

    NotificationErrorType(String message) {this.message = message;}

    public String getMessage() {return message;}
}
