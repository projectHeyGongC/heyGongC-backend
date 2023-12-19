package com.heygongc.member.application;

import com.heygongc.member.domain.Member;
import com.heygongc.member.domain.MemberRepository;
import com.heygongc.member.presentation.request.MemberCreateRequest;
import com.heygongc.member.presentation.request.MemberSnsType;
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
        Member member = new Member("TEST001", MemberSnsType.GOOGLE, "test001@gmail.com", true, true);
        when(memberRepository.save(any())).thenReturn(member);

        // when
        Member result = memberService.createTestMember(new MemberCreateRequest("TEST001", MemberSnsType.GOOGLE, "test001@gmail.com", true, true));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("TEST001");
    }
}