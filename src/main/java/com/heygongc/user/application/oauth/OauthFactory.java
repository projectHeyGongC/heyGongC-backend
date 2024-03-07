package com.heygongc.user.application.oauth;

import com.heygongc.user.domain.type.SnsType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class OauthFactory {

    private final Map<SnsType, OauthProvider> oauthProviders;

    public OauthFactory(Set<OauthProvider> oauthProviders) {
        this.oauthProviders = new HashMap<>();
        oauthProviders.forEach(provider -> this.oauthProviders.put(provider.getOauthName(), provider));
    }

    public OauthUser getOauthUser(SnsType oauthName, String accessToken) {
        return getOauthProvider(oauthName).getOAuthUserInfo(accessToken);
    }

    private OauthProvider getOauthProvider(SnsType oauthName) {
        return oauthProviders.get(oauthName);
    }
}
