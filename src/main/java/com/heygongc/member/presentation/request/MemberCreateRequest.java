package com.heygongc.member.presentation.request;

public record MemberCreateRequest(
        String device_id,
        String id,
        MemberSnsType sns_type,
        String email,
        boolean ads,
        String access_token,
        String refresh_token
) {
}
