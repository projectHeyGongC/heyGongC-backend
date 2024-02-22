package com.heygongc.user.application.apple;

import com.heygongc.global.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(
        name = "AppleOAuth",
        url = "https://appleid.apple.com/auth",
        configuration = FeignConfiguration.class
)
public interface AppleOAuth {

    @GetMapping("/keys")
    ApplePublicKeys getPublicKeys();

}
