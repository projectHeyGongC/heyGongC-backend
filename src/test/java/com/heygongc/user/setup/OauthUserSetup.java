package com.heygongc.user.setup;

import com.heygongc.user.application.oauth.OauthUser;
import com.heygongc.user.domain.type.SnsType;

public class OauthUserSetup {

    public static OauthUser testGoogleUser() {
        return new OauthUser(SnsType.GOOGLE,
                "id" + ((int) (Math.random() * 9999) + 1),
                "test" + ((int) (Math.random() * 9999) + 1) + "@test.com");
    }
    public static OauthUser testGoogleUser(String id) {
        return new OauthUser(SnsType.GOOGLE,
                id,
                "test" + ((int) (Math.random() * 9999) + 1) + "@test.com");
    }
}
