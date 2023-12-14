package com.heygongc.member.presentation;

import com.heygongc.member.application.MemberService;
import com.heygongc.member.domain.Member;
import com.heygongc.member.presentation.request.MemberCreateRequest;
import com.heygongc.member.presentation.response.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberCreateRequest request) {
        Member testMember = memberService.createTestMember(request);
        MemberResponse memberResponse = new MemberResponse(testMember.getId(), testMember.getNickname());
        return ResponseEntity.ok().body(memberResponse);
    }
}
