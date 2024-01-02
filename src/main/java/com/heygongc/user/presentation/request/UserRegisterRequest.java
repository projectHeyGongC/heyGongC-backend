package com.heygongc.user.presentation.request;

public record UserRegisterRequest(
        String deviceId,
        String deviceOs,
        boolean ads,
        TokenRequest token
) {
}
