package com.heygongc.user.presentation.request;

import com.heygongc.user.application.Token;

public record UserLoginRequest(
        Token token
) {
}
