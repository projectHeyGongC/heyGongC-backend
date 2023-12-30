package com.heygongc.user.presentation.request;

public record TokenRequest(
    String accessToken,
    String refreshToken
) {
}