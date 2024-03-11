package com.heygongc.user.application.oauth;

import com.heygongc.user.domain.type.SnsType;

public record OauthUser(SnsType snsType, String id, String email) {
}
