package com.heygongc.member.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heygongc.member.application.MemberService;
import com.heygongc.member.domain.Member;
import com.heygongc.member.presentation.request.MemberCreateRequest;
import com.heygongc.member.presentation.request.MemberLoginRequest;
import com.heygongc.member.presentation.response.MemberResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/members")
public class MemberController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${google.client.redirect-uri}")
    private String GOOGLE_LOGIN_REDIRECT_URI;

    @GetMapping("/login/getGoogleLoginUrl")
    public ResponseEntity<String> getGoogleLoginUrl() {
        String reUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" +
                GOOGLE_CLIENT_ID +
                "&redirect_uri=" +
                GOOGLE_LOGIN_REDIRECT_URI +
                "&response_type=code" +
                "&scope=email profile";
        logger.debug("getGoogleLoginURL >> {}", reUrl);
        return ResponseEntity.ok().body(reUrl);
    }

    @GetMapping("/login/googleLoginCallback")
    public ResponseEntity<MemberResponse> googleLoginCallback(@RequestParam(value = "code") String authCode) throws IOException {
        Member member = memberService.getGoogleMemberInfo(authCode);
        MemberResponse memberResponse = new MemberResponse(member.getSeq(), member.getDevice_id(), member.getId(), member.getEmail(), member.getSns_type(), member.getAccess_token(), member.getRefresh_token());
        logger.debug("googleLoginCallback >> {}", memberResponse.toString());
        return ResponseEntity.ok().body(memberResponse);
    }

    @PostMapping("/isMemberExists")
    public ResponseEntity<Boolean> isMemberExists(@RequestBody MemberLoginRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(memberService.isMemberExists(request));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> loginMember(@RequestBody MemberLoginRequest request) throws JsonProcessingException {
        Member member = memberService.loginMember(request);
        MemberResponse memberResponse = new MemberResponse(member.getSeq(), member.getDevice_id(), member.getId(), member.getEmail(), member.getSns_type(), member.getAccess_token(), member.getRefresh_token());
        return ResponseEntity.ok().body(memberResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberCreateRequest request) {
        Member member = memberService.createMember(request);
        MemberResponse memberResponse = new MemberResponse(member.getSeq(), member.getDevice_id(), member.getId(), member.getEmail(), member.getSns_type(), member.getAccess_token(), member.getRefresh_token());
        return ResponseEntity.ok().body(memberResponse);
    }
}
