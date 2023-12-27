package com.heygongc.user.presentation.request;

import com.heygongc.user.application.Token;

public record UserCreateRequest(
        String deviceId,
        boolean ads,
        Token token
) {
}
