package com.heygongc.member.presentation.request;

public record MemberCreateRequest(
        String id,
        MemberSnsType sns_type,
        String email,
        boolean alarm,
        boolean ads
) {
}
