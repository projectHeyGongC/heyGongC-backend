package com.heygongc.user.presentation.response;

import com.heygongc.user.application.UserSnsType;

public record UserResponse(
        String deviceId,
        String deviceOs,
        UserSnsType snsType,
        String email,
        Boolean alarm,
        Boolean ads
) {
}