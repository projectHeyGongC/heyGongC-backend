package com.heygongc.user.application.presentation.request;

public record UserCreateRequest(
        String id,
        UserSnsType sns_type,
        String email,
        boolean alarm,
        boolean ads
) {
}
