package com.heygongc.user.application;

public record Token(
    String accessToken,
    String refreshToken
) {
}