package com.heygongc.user.presentation.request;

public record RefreshAccessTokenRequest(
    String refreshToken
) {
}