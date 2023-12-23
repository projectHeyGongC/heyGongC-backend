package com.heygongc.user.presentation.request;

import com.heygongc.user.application.Token;

public record UserCreateRequest(
        String device_id,
        boolean ads,
        Token token
) {
}
