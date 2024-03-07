package com.heygongc.user.application.oauth.google;

import com.heygongc.user.application.oauth.OauthProvider;
import com.heygongc.user.application.oauth.OauthUser;
import com.heygongc.user.domain.type.SnsType;
import org.springframework.stereotype.Component;

@Component
public class GoogleOauthProvider implements OauthProvider {

    private final GoogleOAuth googleOAuth;

    public GoogleOauthProvider(GoogleOAuth googleOAuth) {
        this.googleOAuth = googleOAuth;
    }

    @Override
    public OauthUser getOAuthUserInfo(String accessToken) {
        GoogleUserResponse user = googleOAuth.getUser(accessToken);
        return new OauthUser(getOauthName(), user.sub(), user.email());
    }

    @Override
    public SnsType getOauthName() {
        return SnsType.GOOGLE;
    }
}
