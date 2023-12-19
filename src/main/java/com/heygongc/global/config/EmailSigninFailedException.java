package com.heygongc.global.config;

public class EmailSigninFailedException extends RuntimeException{

    public EmailSigninFailedException() {}
    public EmailSigninFailedException(String msg) {
        super(msg);
    }
}
