package com.heygongc.auth.application;

public interface TokenProvider {

    Object extract(String token);
}
