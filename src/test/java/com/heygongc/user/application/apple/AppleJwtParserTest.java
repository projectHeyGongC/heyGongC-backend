package com.heygongc.user.application.apple;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.*;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class AppleJwtParserTest {

    @InjectMocks
    private AppleJwtParser appleJwtParser;

    @Test
    public void 헤더_파싱_테스트() throws NoSuchAlgorithmException {
        // given
        Date now = new Date();
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA")
                .generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        String identityToken = Jwts.builder()
                .setHeaderParam("kid", "W2R4HXF3K")
                .claim("id", "12345678")
                .setIssuer("iss")
                .setIssuedAt(now)
                .setAudience("aud")
                .setExpiration(new Date(now.getTime() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();

        // when
        Map<String, String> actual = appleJwtParser.parseHeaders(identityToken);

        // given
        assertThat(actual).containsKeys("alg", "kid");
    }

    @Test
    public void Claims_반환_테스트() throws NoSuchAlgorithmException {
        String expected = "19281729";
        Date now = new Date();
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA")
                .generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String identityToken = Jwts.builder()
                .setHeaderParam("kid", "W2R4HXF3K")
                .claim("id", "12345678")
                .setIssuer("iss")
                .setIssuedAt(now)
                .setAudience("aud")
                .setSubject(expected)
                .setExpiration(new Date(now.getTime() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();

        Claims claims = appleJwtParser.parseClaims(identityToken, publicKey);

        assertAll(
                () -> assertThat(claims).isNotEmpty(),
                () -> assertThat(claims.getSubject()).isEqualTo(expected)
        );
    }

}