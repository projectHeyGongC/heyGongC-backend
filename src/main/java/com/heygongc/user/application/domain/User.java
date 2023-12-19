package com.heygongc.user.application.domain;

import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.user.application.presentation.request.UserSnsType;
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
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long seq;

    @Column(name = "user_id", length = 200, nullable = false)
    private String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserSnsType sns_type;

    @Column(length = 200, nullable = false)
    private String email;

    private String deleted_at;

    @Column(nullable = false)
    private boolean alarm;

    private String fcm_token;

    @Column(nullable = false)
    private boolean ads;

    public User(String id, UserSnsType sns_type, String email, boolean alarm, boolean ads) {
        this.id = id;
        this.sns_type = sns_type;
        this.email = email;
        this.alarm = alarm;
        this.ads = ads;
    }
}
