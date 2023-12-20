package com.heygongc.member.presentation.response;

import com.heygongc.member.presentation.request.MemberSnsType;

public record MemberResponse(
        Long seq,
        String device_id,
        String id,
        String email,
        MemberSnsType sns_type,
        String access_token,
        String refresh_token
) {
}
