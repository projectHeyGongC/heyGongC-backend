package com.heygongc.user.domain.entity;

import com.heygongc.global.config.BaseTimeEntity;
import com.heygongc.global.type.OsType;
import com.heygongc.user.domain.type.SnsType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED) // No default constructor for entity 오류 해결을 위해 필요
@DynamicUpdate //변경된 필드만 Update
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long seq;

    @Column(name = "sns_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    @Column(name = "sns_id", length = 200, nullable = false)
    private String snsId; //고유식별자

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "user_id", length = 200, nullable = false)
    private String userId;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "device_os", nullable = false)
    @Enumerated(EnumType.STRING)
    private OsType deviceOs;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "alarm", nullable = false)
    private Boolean alarm;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "ads", nullable = false)
    private Boolean ads;

    @Builder(builderMethodName = "createUser")
    public User(SnsType snsType, String snsId, String email, String deviceId, OsType deviceOs, Boolean alarm, Boolean ads) {
        this.snsType = snsType;
        this.snsId = snsId;
        this.email = email;
        this.userId = randomUserId();
        this.deviceId = deviceId;
        this.deviceOs = deviceOs;
        this.alarm = alarm;
        this.ads = ads;
    }

    private static String randomUserId() {
        return "USER" + ((int) (Math.random() * 9999) + 1);
    }

    public void changeDevice(String deviceId, OsType deviceOs) {
        this.deviceId = deviceId;
        this.deviceOs = deviceOs;
    }

    public void reRegister(String deviceId, OsType deviceOs, Boolean ads) {
        this.deviceId = deviceId;
        this.deviceOs = deviceOs;
        this.ads = ads;
        this.deletedAt = null;
    }

    public void withdraw() {
        this.userId = "*****";
        this.snsId = "*****" ;
        this.email = "*****@*****.***";
        this.deviceId = null;
        this.deviceOs = null;
        this.fcmToken = null;
        this.deletedAt = LocalDateTime.now();
    }
}
