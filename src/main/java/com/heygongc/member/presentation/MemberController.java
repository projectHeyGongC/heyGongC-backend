package com.heygongc.member.presentation;

import com.heygongc.member.application.MemberService;
import com.heygongc.member.domain.Member;
import com.heygongc.member.presentation.request.MemberCreateRequest;
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

    @GetMapping("/login/google")
    public ResponseEntity<String> getGoogleLoginURL() {
        String reUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" +
                GOOGLE_CLIENT_ID +
                "&redirect_uri=" +
                GOOGLE_LOGIN_REDIRECT_URI +
                "&response_type=code" +
                "&scope=email profile";
        logger.debug("getGoogleLoginURL >> " + reUrl);
        return ResponseEntity.ok().body(reUrl);
    }

    @GetMapping("/login/google/Callback")
    public ResponseEntity<Member> googleLoginCallback(@RequestParam(value = "code") String authCode) throws IOException {
        return ResponseEntity.ok().body(memberService.googleLogin(authCode));
    }

    @PostMapping("/create")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberCreateRequest request) {
        Member member = memberService.createTestMember(request);
        MemberResponse memberResponse = new MemberResponse(member.getSeq(), member.getId(), member.getEmail());
        return ResponseEntity.ok().body(memberResponse);
    }
}
