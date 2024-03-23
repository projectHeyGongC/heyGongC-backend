package com.heygongc.user.application.oauth.apple;

import com.heygongc.user.application.oauth.OauthProvider;
import com.heygongc.user.application.oauth.OauthUser;
import com.heygongc.user.domain.type.SnsType;
import com.heygongc.user.exception.InvalidUserTokenException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@Component
public class AppleOauthUserProvider implements OauthProvider {

    private final AppleJwtParser appleJwtParser;
    private final AppleOAuth appleOAuth;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleClaimsValidator appleClaimsValidator;

    public AppleOauthUserProvider(AppleJwtParser appleJwtParser, AppleOAuth appleOAuth, ApplePublicKeyGenerator applePublicKeyGenerator, AppleClaimsValidator appleClaimsValidator) {
        this.appleJwtParser = appleJwtParser;
        this.appleOAuth = appleOAuth;
        this.applePublicKeyGenerator = applePublicKeyGenerator;
        this.appleClaimsValidator = appleClaimsValidator;
    }

    private void validateClaims(Claims claims) {
        if (!appleClaimsValidator.isValid(claims)) {
            throw new InvalidUserTokenException("Apple OAuth Claims 값이 올바르지 않습니다.");
        }
    }

    @Override
    public OauthUser getOAuthUserInfo(String accessToken) {
        Map<String, String> headers = appleJwtParser.parseHeaders(accessToken);
        ApplePublicKeys applePublicKeys = appleOAuth.getPublicKeys();
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, applePublicKeys);
        Claims claims = appleJwtParser.parseClaims(accessToken, publicKey);
        validateClaims(claims);
        return new OauthUser(getOauthName(), claims.getSubject(), claims.get("email", String.class));
    }

    @Override
    public SnsType getOauthName() {
        return SnsType.APPLE;
    }
}
