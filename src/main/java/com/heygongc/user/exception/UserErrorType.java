package com.heygongc.user.exception;

public enum UserErrorType {

    USER_NOT_FOUND("사용자가 존재하지 않습니다."),
    ALREADY_SIGN_IN("이미 로그인한 사용자입니다."),
    ALREADY_SIGN_UP("이미 가입한 사용자입니다."),
    ALREADY_LEFT("이미 탈퇴한 사용자입니다."),
    NEW_LOGIN_DETECTED("새로운 로그인이 감지되었습니다."),
    ;


    private final String message;

    UserErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
