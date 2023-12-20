package com.heygongc.member.domain;

import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.member.presentation.request.MemberSnsType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor // Builder 어노테이션을 위해 필요
@NoArgsConstructor // No default constructor for entity 오류 해결을 위해 필요
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private Long seq;

    @Column(nullable = false)
    private String device_id;

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

    private String access_token;

    private String refresh_token;
}
