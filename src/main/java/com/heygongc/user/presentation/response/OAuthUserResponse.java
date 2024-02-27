package com.heygongc.user.presentation.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OAuthUserResponse(
    String sub,
    String email
) {
}