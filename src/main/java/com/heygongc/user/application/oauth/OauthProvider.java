package com.heygongc.user.application.oauth;

import com.heygongc.user.domain.type.SnsType;

public interface OauthProvider {

    OauthUser getOAuthUserInfo(String accessToken);

    SnsType getOauthName();
}
