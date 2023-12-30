package com.heygongc.user.exception;

public class EmailSigninFailedException extends RuntimeException{

    public EmailSigninFailedException() {}
    public EmailSigninFailedException(String msg) {
        super(msg);
    }
}
