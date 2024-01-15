package com.heygongc.user.domain;

import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.user.application.UserSnsType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor // Builder 어노테이션을 위해 필요
@NoArgsConstructor(access=PROTECTED) // No default constructor for entity 오류 해결을 위해 필요
@DynamicUpdate //변경된 필드만 Update
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long seq;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "device_os", nullable = false)
    private String deviceOs;

    @Column(name = "user_id", length = 200, nullable = false)
    private String id;

    @Column(name = "sns_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserSnsType snsType;

    @Column(name = "sns_id", length = 200, nullable = false)
    private String snsId; //고유식별자

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "alarm", nullable = false)
    private Boolean alarm;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "ads", nullable = false)
    private Boolean ads;
}
