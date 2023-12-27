package com.heygongc.user.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenResponse (
    @JsonProperty("access_token")
    String accessToken,    // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰
    @JsonProperty("expires_in")
    String expiresIn,      // Access Token의 남은 수명
    @JsonProperty("refresh_token")
    String refreshToken,   // 새 액세스 토큰을 얻는 데 사용할 수 있는 토큰
    String scope,
    @JsonProperty("token_type")
    String tokenType,      // 반환된 토큰 유형(Bearer 고정)
    @JsonProperty("id_token")
    String idToken
) {
}