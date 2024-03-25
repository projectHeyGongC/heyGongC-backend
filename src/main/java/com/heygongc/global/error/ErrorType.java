package com.heygongc.global.error;

public enum ErrorType {

    UNAUTHENTICATED("인증이 실패하였습니다."),
    FORBIDDEN("권한이 없습니다."),
    BAD_REQUEST("잘못된 요청입니다."),
    EXPIRED_TOKEN ("만료된 토큰입니다."),
    INTERNAL_SERVER_ERROR("서버에서 에러가 발생하였습니다."),
    ;


    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
