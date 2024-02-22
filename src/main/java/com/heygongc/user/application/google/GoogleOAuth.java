package com.heygongc.user.application.google;

import com.heygongc.global.config.FeignConfiguration;
import com.heygongc.user.presentation.response.OAuthUserResponse;
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
public interface GoogleOAuth {

    @PostMapping(value = "/tokeninfo")
    OAuthUserResponse getUser(@RequestParam("access_token") String accessToken);

}