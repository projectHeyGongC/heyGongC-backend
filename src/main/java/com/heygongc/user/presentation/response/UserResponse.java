package com.heygongc.user.presentation.response;

import com.heygongc.user.presentation.request.UserSnsType;

public record UserResponse(
        Long seq,
        String device_id,
        String id,
        String email,
        UserSnsType sns_type,
        String access_token,
        String refresh_token
) {
}
