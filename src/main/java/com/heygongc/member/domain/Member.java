package com.heygongc.member.domain;

import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.member.presentation.request.MemberSnsType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private Long seq;

    @Column(name = "member_id", length = 200, nullable = false)
    private String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberSnsType sns_type;

    @Column(length = 200, nullable = false)
    private String email;

    private String deleted_at;

    @Column(nullable = false)
    private boolean alarm;

    private String fcm_token;

    @Column(nullable = false)
    private boolean ads;

    public Member(String id, MemberSnsType sns_type, String email, boolean alarm, boolean ads) {
        this.id = id;
        this.sns_type = sns_type;
        this.email = email;
        this.alarm = alarm;
        this.ads = ads;
    }
}
