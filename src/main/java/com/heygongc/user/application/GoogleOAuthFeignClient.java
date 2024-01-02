package com.heygongc.user.application;

import com.heygongc.global.config.FeignConfiguration;
import com.heygongc.user.presentation.response.GoogleTokenResponse;
import com.heygongc.user.presentation.response.GoogleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(
        name = "GoogleOAuth",
        url = "https://oauth2.googleapis.com",
        configuration = FeignConfiguration.class
)
public interface GoogleOAuthFeignClient {

    @PostMapping(value = "/token")
    GoogleTokenResponse getToken(@RequestParam("code") String code,
                                 @RequestParam("client_id") String clientId,
                                 @RequestParam("client_secret") String clientSecret,
                                 @RequestParam("redirect_uri") String redirectUri,
                                 @RequestParam("grant_type") String grantType);

    @PostMapping(value = "/tokeninfo")
    GoogleUserResponse getUser(@RequestParam("access_token") String accessToken);

}