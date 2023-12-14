package com.heygongc.member.application;

import com.heygongc.member.domain.Member;
import com.heygongc.member.domain.MemberRepository;
import com.heygongc.member.presentation.request.MemberCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    public void 회원테스트() {
        // given
        Member member = new Member(1L, "테스트");
        when(memberRepository.save(any())).thenReturn(member);

        // when
        Member result = memberService.createTestMember(new MemberCreateRequest("테스트"));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNickname()).isEqualTo("테스트");
    }
}