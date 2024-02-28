package com.heygongc.user.application.apple;

import com.heygongc.user.exception.InvalidTokenException;
import com.heygongc.user.presentation.response.OAuthUserResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.security.*;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AppleOAuthUserProviderTest {

    @InjectMocks
    private AppleOAuthUserProvider appleOAuthUserProvider;

    @Mock
    private AppleJwtParser appleJwtParser;

    @Mock
    private AppleOAuth appleOAuth;

    @Mock
    private ApplePublicKeyGenerator applePublicKeyGenerator;

    @Mock
    private AppleClaimsValidator appleClaimsValidator;

    @Test
    @DisplayName("Apple OAuth 유저 접속 시 sub와 email 값을 반환한다")
    void getApplePlatformMember() throws NoSuchAlgorithmException {
        String expected = "19281729";
        Date now = new Date();
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA")
                .generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String identityToken = Jwts.builder()
                .setHeaderParam("kid", "W2R4HXF3K")
                .claim("id", "12345678")
                .claim("email", "kth@apple.com")
                .setIssuer("iss")
                .setIssuedAt(now)
                .setAudience("aud")
                .setSubject(expected)
                .setExpiration(new Date(now.getTime() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();

        when(appleJwtParser.parseHeaders(any())).thenReturn(new HashMap<String, String>());
        when(appleOAuth.getPublicKeys()).thenReturn(mock(ApplePublicKeys.class));
        when(applePublicKeyGenerator.generatePublicKey(any(), any())).thenReturn(publicKey);
        when(appleClaimsValidator.isValid(any())).thenReturn(true);
        when(appleJwtParser.parseClaims(any(), any())).thenReturn(Jwts.parser()
                                                                    .setSigningKey(publicKey)
                                                                    .parseClaimsJws(identityToken)
                                                                    .getBody());

        OAuthUserResponse user = appleOAuthUserProvider.getApplePlatformMember(identityToken);

        assertThat(user.sub()).isEqualTo(expected);
        assertThat(user.email()).isEqualTo("kth@apple.com");
    }

    @Test
    @DisplayName("Claim 검증에 실패할 경우 예외를 반환한다")
    void invalidClaims() throws NoSuchAlgorithmException {
        Date now = new Date();
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA")
                .generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String identityToken = Jwts.builder()
                .setHeaderParam("kid", "W2R4HXF3K")
                .claim("id", "12345678")
                .claim("email", "kth@apple.com")
                .setIssuer("iss")
                .setIssuedAt(now)
                .setAudience("aud")
                .setSubject("19281729")
                .setExpiration(new Date(now.getTime() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();

        when(appleJwtParser.parseHeaders(any())).thenReturn(new HashMap<String, String>());
        when(appleOAuth.getPublicKeys()).thenReturn(mock(ApplePublicKeys.class));
        when(applePublicKeyGenerator.generatePublicKey(any(), any())).thenReturn(publicKey);
        when(appleClaimsValidator.isValid(any())).thenReturn(false);
        when(appleJwtParser.parseClaims(any(), any())).thenReturn(Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(identityToken)
                .getBody());

        assertThrows(InvalidTokenException.class, () -> appleOAuthUserProvider.getApplePlatformMember(identityToken));
    }
}
