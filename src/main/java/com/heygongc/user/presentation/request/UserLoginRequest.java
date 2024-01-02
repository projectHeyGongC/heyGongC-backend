package com.heygongc.user.presentation.request;

public record UserLoginRequest(
        String deviceId,
        String deviceOs,
        TokenRequest token
) {
}
