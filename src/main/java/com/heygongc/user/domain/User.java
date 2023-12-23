package com.heygongc.user.domain;

import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.user.application.UserSnsType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor // Builder 어노테이션을 위해 필요
@NoArgsConstructor // No default constructor for entity 오류 해결을 위해 필요
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long seq;

    @Column(nullable = false)
    private String device_id;

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

    private String refresh_token;

    private LocalDateTime refresh_create;

    private LocalDateTime refresh_expire;
}
