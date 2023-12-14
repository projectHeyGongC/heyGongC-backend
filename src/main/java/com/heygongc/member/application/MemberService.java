package com.heygongc.member.application;

import com.heygongc.member.domain.Member;
import com.heygongc.member.domain.MemberRepository;
import com.heygongc.member.presentation.request.MemberCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member createTestMember(MemberCreateRequest request) {
        Member member = new Member(request.nickname());
        return memberRepository.save(member);
    }
}
