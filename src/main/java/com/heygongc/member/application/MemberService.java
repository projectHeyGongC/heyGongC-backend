package com.heygongc.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heygongc.global.config.EmailSigninFailedException;
import com.heygongc.member.domain.GoogleOAuth;
import com.heygongc.member.domain.Member;
import com.heygongc.member.domain.MemberRepository;
import com.heygongc.member.presentation.request.MemberCreateRequest;
import com.heygongc.member.presentation.request.MemberSnsType;
import com.heygongc.member.presentation.response.GoogleTokenResponse;
import com.heygongc.member.presentation.response.GoogleUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class MemberService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoogleOAuth googleOAuth;
    private final MemberRepository memberRepository;

    public MemberService(GoogleOAuth googleOAuth, MemberRepository memberRepository) {
        this.googleOAuth = googleOAuth;
        this.memberRepository = memberRepository;
    }

    public Member googleLogin(String authCode) throws IOException {
        GoogleUserResponse googleUser = getGoogleUserInfo(authCode);

        if(!memberRepository.existsByEmail(googleUser.getEmail())) {
            memberRepository.save(
                    Member.builder()
                            .email(googleUser.getEmail())
                            .id(googleUser.getName())
                            .sns_type(MemberSnsType.GOOGLE)
                            .alarm(true)
                            .ads(true)
                            .build()
            );
        }
        return memberRepository.findByEmail(
                googleUser.getEmail()
        ).orElseThrow(EmailSigninFailedException::new);
    }

    public GoogleUserResponse getGoogleUserInfo(String authCode) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(authCode);
        GoogleTokenResponse token = googleOAuth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(token);
        GoogleUserResponse googleUser = googleOAuth.getUserInfo(userInfoResponse);
        return googleUser;
    }

    @Transactional
    public Member createTestMember(MemberCreateRequest request) {
        Member member = new Member(
                request.id(),
                request.sns_type(),
                request.email(),
                request.alarm(),
                request.ads()
        );
        return memberRepository.save(member);
    }
}
