package com.heygongc.user.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthToken {
    private String accessToken;
    private String refreshToken;
}
