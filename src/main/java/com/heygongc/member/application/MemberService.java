package com.heygongc.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heygongc.global.config.EmailSigninFailedException;
import com.heygongc.member.domain.GoogleOAuth;
import com.heygongc.member.domain.Member;
import com.heygongc.member.domain.MemberRepository;
import com.heygongc.member.presentation.request.MemberCreateRequest;
import com.heygongc.member.presentation.request.MemberLoginRequest;
import com.heygongc.member.presentation.request.MemberSnsType;
import com.heygongc.member.presentation.response.GoogleMemberResponse;
import com.heygongc.member.presentation.response.GoogleTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoogleOAuth googleOAuth;
    private final MemberRepository memberRepository;

    public MemberService(GoogleOAuth googleOAuth, MemberRepository memberRepository) {
        this.googleOAuth = googleOAuth;
        this.memberRepository = memberRepository;
    }

    public Member getGoogleMemberInfo(String authCode) throws JsonProcessingException {
        ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(authCode);
        GoogleTokenResponse token = googleOAuth.getAccessToken(accessTokenResponse);
        ResponseEntity<String> memberInfoResponse = googleOAuth.requestMemberInfo(token);
        GoogleMemberResponse googleMember = googleOAuth.getMemberInfo(memberInfoResponse);
        return memberRepository.findByEmail(googleMember.getEmail())
                .orElse(Member.builder()
                        .email(googleMember.getEmail())
                        .id(googleMember.getEmail()) //추후 변경 필요(ID값 어떻게 처리할지?)
                        .sns_type(MemberSnsType.GOOGLE)
                        .access_token(token.getAccess_token())
                        .refresh_token(token.getRefresh_token())
                        .build()
                );
    }

    public Boolean isMemberExists(MemberLoginRequest request) throws JsonProcessingException {
        if(request.seq() == null) return false;
        return memberRepository.existsById(request.seq());
    }

    @Transactional
    public Member loginMember(MemberLoginRequest request) throws JsonProcessingException {
        if(!isMemberExists(request)) { throw new EmailSigninFailedException(); }

        return memberRepository.findById(request.seq()).orElseThrow(EmailSigninFailedException::new);
    }

    @Transactional
    public Member createMember(MemberCreateRequest request) {
        if(memberRepository.existsByEmail(request.email())) { throw new EmailSigninFailedException(); }
        return memberRepository.save(
                Member.builder()
                        .device_id(request.device_id())
                        .email(request.email())
                        .id(request.id())
                        .sns_type(request.sns_type())
                        .alarm(true)
                        .ads(request.ads())
                        .access_token(request.access_token())
                        .refresh_token(request.refresh_token())
                        .build()
        );
    }
}
