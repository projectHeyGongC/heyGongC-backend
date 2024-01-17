package com.heygongc.user.application;

import com.heygongc.user.presentation.response.GoogleTokenResponse;
import com.heygongc.user.presentation.response.GoogleUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuth {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoogleOAuthFeignClient googleOAuthFeignClient;

    public GoogleOAuth(GoogleOAuthFeignClient googleOAuthFeignClient) {
        this.googleOAuthFeignClient = googleOAuthFeignClient;
    }

    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;

    public String getLoginUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth?"
                + "client_id=" + GOOGLE_CLIENT_ID
                + "&redirect_uri=http://localhost:8080/v1/user/google/loginCallback"
                + "&response_type=code"
                + "&scope=email profile"
                + "&access_type=offline"
                + "&prompt=consent";
    }

    public GoogleTokenResponse getToken(String code) {
        return googleOAuthFeignClient.getToken(code,
                                                GOOGLE_CLIENT_ID,
                                                GOOGLE_CLIENT_SECRET,
                                                "http://localhost:8080/v1/user/google/loginCallback",
                                                "authorization_code");
    }

    public GoogleUserResponse getUser(String token) {
        return googleOAuthFeignClient.getUser(token);
    }
}