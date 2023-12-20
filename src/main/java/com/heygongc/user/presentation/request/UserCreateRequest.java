package com.heygongc.user.presentation.request;

public record UserCreateRequest(
        String device_id,
        String id,
        UserSnsType sns_type,
        String email,
        boolean ads,
        String access_token,
        String refresh_token
) {
}
