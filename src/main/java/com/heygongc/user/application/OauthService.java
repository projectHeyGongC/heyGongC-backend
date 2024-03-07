package com.heygongc.user.application;

import com.heygongc.global.utils.EnumUtils;
import com.heygongc.user.application.oauth.OauthFactory;
import com.heygongc.user.application.oauth.OauthUser;
import com.heygongc.user.domain.type.SnsType;
import org.springframework.stereotype.Service;

@Service
public class OauthService {

    private final OauthFactory oAuthFactory;

    public OauthService(OauthFactory oAuthFactory) {
        this.oAuthFactory = oAuthFactory;
    }

    public OauthUser getOAuthUser(String snsType, String accessToken) {
        return oAuthFactory.getOauthUser(EnumUtils.getEnumConstant(SnsType.class, snsType), accessToken);
    }
}
