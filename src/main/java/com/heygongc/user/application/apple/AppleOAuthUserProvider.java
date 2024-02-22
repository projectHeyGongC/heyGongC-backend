package com.heygongc.user.application.apple;

import com.heygongc.user.exception.InvalidTokenException;
import com.heygongc.user.presentation.response.OAuthUserResponse;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@Component
public class AppleOAuthUserProvider {

    private final AppleJwtParser appleJwtParser;
    private final AppleOAuth appleOAuth;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleClaimsValidator appleClaimsValidator;

    public AppleOAuthUserProvider(AppleJwtParser appleJwtParser, AppleOAuth appleOAuth, ApplePublicKeyGenerator applePublicKeyGenerator, AppleClaimsValidator appleClaimsValidator) {
        this.appleJwtParser = appleJwtParser;
        this.appleOAuth = appleOAuth;
        this.applePublicKeyGenerator = applePublicKeyGenerator;
        this.appleClaimsValidator = appleClaimsValidator;
    }

    public OAuthUserResponse getApplePlatformMember(String identityToken) {
        Map<String, String> headers = appleJwtParser.parseHeaders(identityToken);
        ApplePublicKeys applePublicKeys = appleOAuth.getPublicKeys();
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, applePublicKeys);
        Claims claims = appleJwtParser.parseClaims(identityToken, publicKey);
        validateClaims(claims);
        return new OAuthUserResponse(claims.getSubject(), claims.get("email", String.class));
    }

    private void validateClaims(Claims claims) {
        if (!appleClaimsValidator.isValid(claims)) {
            throw new InvalidTokenException("Apple OAuth Claims 값이 올바르지 않습니다.");
        }
    }
}
