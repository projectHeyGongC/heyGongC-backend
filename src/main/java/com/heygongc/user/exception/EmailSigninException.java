package com.heygongc.user.exception;

public class EmailSigninException extends RuntimeException{

    public EmailSigninException() {}
    public EmailSigninException(String msg) {
        super(msg);
    }
}
