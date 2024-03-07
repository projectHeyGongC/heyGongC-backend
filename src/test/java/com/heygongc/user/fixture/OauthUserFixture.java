package com.heygongc.user.fixture;

import com.heygongc.user.application.oauth.OauthUser;
import com.heygongc.user.domain.type.SnsType;

public class OauthUserFixture {

    public static OauthUser testGoogleUser() {
        return new OauthUser(SnsType.GOOGLE, "123456789", "test@test.com");
    }
}
