package com.heygongc.user.presentation.response;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {
}