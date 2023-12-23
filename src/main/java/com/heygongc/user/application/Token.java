package com.heygongc.user.application;

public record Token(
    String access_token,
    String refresh_token
) {
}