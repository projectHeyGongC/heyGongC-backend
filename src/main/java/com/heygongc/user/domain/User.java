package com.heygongc.user.domain;

import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.user.application.UserSnsType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor // Builder 어노테이션을 위해 필요
@NoArgsConstructor(access=PROTECTED) // No default constructor for entity 오류 해결을 위해 필요
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long seq;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "user_id", length = 200, nullable = false)
    private String id;

    @Column(name = "sns_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserSnsType snsType;

    @Column(name = "email", length = 200, nullable = false)
    private String email;

    @Column(name = "deleted_at")
    private String deletedAt;

    @Column(name = "alarm", nullable = false)
    private boolean alarm;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "ads", nullable = false)
    private boolean ads;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "refresh_create")
    private LocalDateTime refreshCreate;

    @Column(name = "refresh_expire")
    private LocalDateTime refreshExpire;
}
