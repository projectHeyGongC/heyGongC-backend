package com.heygongc.user.presentation.response;

public record UserResponse(
        String deviceId,
        String deviceOs,
        String snsType,
        String email,
        Boolean alarm,
        Boolean ads
) {
}