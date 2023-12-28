package com.heygongc.global.error;

import com.heygongc.global.error.exception.ForbiddenException;
import com.heygongc.global.error.exception.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * 인증을 실패 했을 경우 예외 처리
     */
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public HttpEntity<ErrorResponse> handler(UnauthenticatedException e) {
        return new HttpEntity<>(ErrorResponse.from(e.getCode(), e.getMessage()));
    }

    /**
     * 권한이 없을 경우 예외 처리
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public HttpEntity<ErrorResponse> handler(ForbiddenException e) {
        return new HttpEntity<>(ErrorResponse.from(e.getCode(), e.getMessage()));
    }

    /**
     * 서버에서 에러가 발생 했을 경우 예외 처리
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpEntity<ErrorResponse> handler(Exception e) {
        log.error(e.getMessage());
        return new HttpEntity<>(ErrorResponse.from(ErrorType.INTERNAL_SERVER_ERROR.name(), ErrorType.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
